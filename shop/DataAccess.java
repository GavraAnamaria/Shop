package shop;

import java.lang.reflect.*;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataAccess <T> {
    protected static final Logger LOGGER = Logger.getLogger(DataAccess.class.getName());
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public DataAccess() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    private String createSelectQuery(String field) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT ");
        str.append(" * ");
        str.append(" FROM ");
        str.append(type.getSimpleName());
        str.append(" WHERE " + field + " =?");
        return str.toString();
    }

    public List<T> findAll() {
        String s = "SELECT * FROM " + "magazin." + type.getSimpleName();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = DBconnection.getConnection();
            statement = connection.prepareStatement(s);
            resultSet = statement.executeQuery();
            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + ":findAll " + e.getMessage());
        } finally {
            DBconnection.close(resultSet);
            DBconnection.close(statement);
            DBconnection.close(connection);
        }
        return null;
    }

    public List<String> getCol() {
        List<String> names = new ArrayList<>();
        for(Field field: type.getDeclaredFields())
            names.add(field.getName());
        return names;
    }

    public Object[][] getData() {
        try {
            List<T> data = findAll();
            Field[] fields = type.getDeclaredFields();
            Object[][] table = new Object[data.size()][type.getDeclaredFields().length];
            for (int i = 0; i < data.size(); i++) {
                for (int j = 0; j < fields.length; j++) {
                    fields[j].setAccessible(true);
                    table[i][j] = fields[j].get(data.get(i));
                }
            }
            return table;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = DBconnection.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            DBconnection.close(resultSet);
            DBconnection.close(statement);
            DBconnection.close(connection);
        }
        return null;
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    public T insert(T t) {
        String s = "INSERT INTO " +  "magazin."+ type.getSimpleName() + " VALUES ( ";
        int i = 0;
        for (Field field : type.getDeclaredFields()) {
            i++;
            String fieldName = field.getName();
            for (Method method : type.getMethods()) {
                try{
                    if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3)))
                        if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                            s = s + "'" + method.invoke(t) + "'";
                            if (i < type.getDeclaredFields().length)
                                s = s + ", ";
                            else {
                                s = s + " )";
                            }
                        }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = DBconnection.getConnection();
            statement = connection.prepareStatement(s);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DA:INSERT " + e.getMessage());
            return null;
        } finally {
            System.out.println(s);
            DBconnection.close(statement);
            DBconnection.close(connection);
        }
        return t;
    }

    public int verificareID(int id){
        List<T> l = findAll();
        if(l.size() != 0)
            for(T t: l)
                for (Method method : type.getMethods())
                    if (method.getName() =="getId")
                        try {
                            if (method.invoke(t).equals(id))
                                return 1;
                        }catch (Exception e){
                            System.out.println("Illegal Acces - identificare id\n");
                        }
        return 0;
    }


    public T update(T t) {
        int id = 0;
        for (Field field : type.getDeclaredFields()) {
            String s = "Update " + type.getSimpleName() + " SET ";
            String fieldName = field.getName();
            if (fieldName.equals("id")) {
                for (Method method : type.getMethods()) {
                    try {
                        if (method.getName().startsWith("getId")) {
                            id = (Integer) method.invoke(t);
                            if (verificareID(id) == 0) {
                                LOGGER.log(Level.WARNING, "Update - Nu a fost gasit niciun client cu acest ID");
                                return null;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else{
                for (Method method : type.getMethods()) {
                    if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3)))
                        if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                            try {
                                s = s + fieldName + " = " + "'" + method.invoke(t) + "'" + " WHERE id = " + id;
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                }
                PreparedStatement statement = null;
                ResultSet resultSet = null;
                Connection connection = null;
                try {
                    connection = DBconnection.getConnection();
                    statement = connection.prepareStatement(s);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, type.getName() + "DA:UPDATE " + e.getMessage());
                } finally {
                    System.out.println(s);
                    DBconnection.close(statement);
                    DBconnection.close(connection);
                }
            }}
        return t;
    }

    public int delete(int id) {
        String s = "DELETE FROM " + type.getSimpleName() + " WHERE id = " +id;
        if(verificareID(id) == 0) {
            LOGGER.log(Level.WARNING, type.getName() + "Delete - nu a fot gasita nicio persoana cu acest id");
            return 0;
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = DBconnection.getConnection();
            statement = connection.prepareStatement(s);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DA:Delete " + e.getMessage());
            return 0;
        } finally {
            DBconnection.close(statement);
            DBconnection.close(connection);
        }
        return 1;
    }
}


