package com.wksc.framwork.platform.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.lang.reflect.Field;

/**
 * Preference类型配置文件操作类
 *
 * @author wanglin@gohighedu.com
 * @date 2013-5-31 上午10:35:37
 */
public class PreferenceConfig implements IConfig {
    private static IConfig mPreferenceConfig;

    private Context mContext;

    private Editor edit = null;

    private SharedPreferences mSharedPreferences;

    private String filename = "microchild";

    private Boolean isLoad = false;

    private PreferenceConfig(Context context) {
        this.mContext = context;

    }

    /**
     * 获得系统资源类
     * 
     * @param context
     * @return
     */
    public static IConfig getPreferenceConfig(Context context) {
        if (mPreferenceConfig == null) {
            mPreferenceConfig = new PreferenceConfig(context);
        }
        return mPreferenceConfig;
    }
    
    @SuppressLint("WorldWriteableFiles")
    @SuppressWarnings("deprecation")
    @Override
    public void loadConfig() {
        
        // TODO Auto-generated method stub
        try {
            mSharedPreferences = mContext.getSharedPreferences(filename,
                    Context.MODE_WORLD_WRITEABLE);
            edit = mSharedPreferences.edit();
            isLoad = true;
        }
        catch (Exception e) {
            // TODO: handle exception
            isLoad = false;
        }
        
    }
    
    @Override
    public Boolean isLoadConfig() {
        // TODO Auto-generated method stub
        return isLoad;
    }
    
    @Override
    public void close() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean isClosed() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void setString(String key, String value) {
        // TODO Auto-generated method stub
        edit.putString(key, value);
        edit.commit();
    }
    
    @Override
    public void setInt(String key, int value) {
        // TODO Auto-generated method stub
        edit.putInt(key, value);
        edit.commit();
    }
    
    @Override
    public void setBoolean(String key, Boolean value) {
        // TODO Auto-generated method stub
        edit.putBoolean(key, value);
        edit.commit();
    }
    
    @Override
    public void setByte(String key, byte[] value) {
        // TODO Auto-generated method stub
        setString(key, String.valueOf(value));
    }
    
    @Override
    public void setShort(String key, short value) {
        // TODO Auto-generated method stub
        setString(key, String.valueOf(value));
    }
    
    @Override
    public void setLong(String key, long value) {
        // TODO Auto-generated method stub
        edit.putLong(key, value);
        edit.commit();
    }
    
    @Override
    public void setFloat(String key, float value) {
        // TODO Auto-generated method stub
        edit.putFloat(key, value);
        edit.commit();
    }
    
    @Override
    public void setDouble(String key, double value) {
        // TODO Auto-generated method stub
        setString(key, String.valueOf(value));
    }
    
    @Override
    public void setString(int resID, String value) {
        // TODO Auto-generated method stub
        setString(this.mContext.getString(resID), value);
        
    }
    
    @Override
    public void setInt(int resID, int value) {
        // TODO Auto-generated method stub
        setInt(this.mContext.getString(resID), value);
    }
    
    @Override
    public void setBoolean(int resID, Boolean value) {
        // TODO Auto-generated method stub
        setBoolean(this.mContext.getString(resID), value);
    }
    
    @Override
    public void setByte(int resID, byte[] value) {
        // TODO Auto-generated method stub
        setByte(this.mContext.getString(resID), value);
    }
    
    @Override
    public void setShort(int resID, short value) {
        // TODO Auto-generated method stub
        setShort(this.mContext.getString(resID), value);
    }
    
    @Override
    public void setLong(int resID, long value) {
        // TODO Auto-generated method stub
        setLong(this.mContext.getString(resID), value);
    }
    
    @Override
    public void setFloat(int resID, float value) {
        // TODO Auto-generated method stub
        setFloat(this.mContext.getString(resID), value);
    }
    
    @Override
    public void setDouble(int resID, double value) {
        // TODO Auto-generated method stub
        setDouble(this.mContext.getString(resID), value);
    }
    
    @Override
    public String getString(String key, String defaultValue) {
        // TODO Auto-generated method stub
        return mSharedPreferences.getString(key, defaultValue);
    }
    
    @Override
    public int getInt(String key, int defaultValue) {
        // TODO Auto-generated method stub
        return mSharedPreferences.getInt(key, defaultValue);
    }
    
    @Override
    public boolean getBoolean(String key, Boolean defaultValue) {
        // TODO Auto-generated method stub
        return mSharedPreferences.getBoolean(key, defaultValue);
    }
    
    @Override
    public byte[] getByte(String key, byte[] defaultValue) {
        // TODO Auto-generated method stub
        try {
            return getString(key, "").getBytes();
        }
        catch (Exception e) {
            // TODO: handle exception
        }
        return defaultValue;
    }
    
    @Override
    public short getShort(String key, Short defaultValue) {
        // TODO Auto-generated method stub
        try {
            return Short.valueOf(getString(key, ""));
        }
        catch (Exception e) {
            // TODO: handle exception
        }
        return defaultValue;
    }
    
    @Override
    public long getLong(String key, Long defaultValue) {
        // TODO Auto-generated method stub
        return mSharedPreferences.getLong(key, defaultValue);
    }
    
    @Override
    public float getFloat(String key, Float defaultValue) {
        // TODO Auto-generated method stub
        return mSharedPreferences.getFloat(key, defaultValue);
    }
    
    @Override
    public double getDouble(String key, Double defaultValue) {
        // TODO Auto-generated method stub
        try {
            return Double.valueOf(getString(key, ""));
        }
        catch (Exception e) {
            // TODO: handle exception
        }
        return defaultValue;
    }
    
    @Override
    public String getString(int resID, String defaultValue) {
        // TODO Auto-generated method stub
        return getString(this.mContext.getString(resID), defaultValue);
    }
    
    @Override
    public int getInt(int resID, int defaultValue) {
        // TODO Auto-generated method stub
        return getInt(this.mContext.getString(resID), defaultValue);
    }
    
    @Override
    public boolean getBoolean(int resID, Boolean defaultValue) {
        // TODO Auto-generated method stub
        return getBoolean(this.mContext.getString(resID), defaultValue);
    }
    
    @Override
    public byte[] getByte(int resID, byte[] defaultValue) {
        // TODO Auto-generated method stub
        return getByte(this.mContext.getString(resID), defaultValue);
    }
    
    @Override
    public short getShort(int resID, Short defaultValue) {
        // TODO Auto-generated method stub
        return getShort(this.mContext.getString(resID), defaultValue);
    }
    
    @Override
    public long getLong(int resID, Long defaultValue) {
        // TODO Auto-generated method stub
        return getLong(this.mContext.getString(resID), defaultValue);
    }
    
    @Override
    public float getFloat(int resID, Float defaultValue) {
        // TODO Auto-generated method stub
        return getFloat(this.mContext.getString(resID), defaultValue);
    }
    
    @Override
    public double getDouble(int resID, Double defaultValue) {
        // TODO Auto-generated method stub
        return getDouble(this.mContext.getString(resID), defaultValue);
    }
    
    @Override
    public void setConfig(Object entity) {
        // TODO Auto-generated method stub
        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            
            if (!ReflectUtils.isTransient(field)) {
                if (ReflectUtils.isBaseDateType(field)) {
                    String columnName = ReflectUtils.getFieldName(field);
                    field.setAccessible(true);
                    setValue(field, columnName, entity);
                }
            }
        }
    }
    
    private void setValue(Field field, String columnName, Object entity) {
        try {
            Class<?> clazz = field.getType();
            if (clazz.equals(String.class)) {
                setString(columnName, (String) field.get(entity));
            }
            else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                setInt(columnName, (Integer) field.get(entity));
            }
            else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
                setFloat(columnName, (Float) field.get(entity));
            }
            else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
                setDouble(columnName, (Double) field.get(entity));
            }
            else if (clazz.equals(Short.class) || clazz.equals(Short.class)) {
                setShort(columnName, (Short) field.get(entity));
            }
            else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                setLong(columnName, (Long) field.get(entity));
            }
            else if (clazz.equals(Boolean.class)) {
                setBoolean(columnName, (Boolean) field.get(entity));
            }
        }
        catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    @Override
    public <T> T getConfig(Class<T> clazz) {
        // TODO Auto-generated method stub
        Field[] fields = clazz.getDeclaredFields();
        T entity = null;
        try {
            entity = (T) clazz.newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                if (!ReflectUtils.isTransient(field)) {
                    if (ReflectUtils.isBaseDateType(field)) {
                        String columnName = ReflectUtils.getFieldName(field);
                        field.setAccessible(true);
                        getValue(field, columnName, entity);
                    }
                }
                
            }
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return entity;
    }
    
    private <T> void getValue(Field field, String columnName, T entity) {
        try {
            Class<?> clazz = field.getType();
            if (clazz.equals(String.class)) {
                field.set(entity, getString(columnName, ""));
            }
            else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                field.set(entity, getInt(columnName, 0));
            }
            else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
                field.set(entity, getFloat(columnName, 0f));
            }
            else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
                field.set(entity, getDouble(columnName, 0.0));
            }
            else if (clazz.equals(Short.class) || clazz.equals(Short.class)) {
                field.set(entity, getShort(columnName, (short) 0));
            }
            else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                field.set(entity, getLong(columnName, 0l));
            }
            else if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
                field.set(entity, getByte(columnName, new byte[8]));
            }
            else if (clazz.equals(Boolean.class)) {
                field.set(entity, getBoolean(columnName, false));
            }
        }
        catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void remove(String key) {
        // TODO Auto-generated method stub
        edit.remove(key);
        edit.commit();
    }
    
    @Override
    public void remove(String... keys) {
        // TODO Auto-generated method stub
        for (String key : keys)
            remove(key);
    }
    
    @Override
    public void clear() {
        // TODO Auto-generated method stub
        edit.clear();
        edit.commit();
    }
    
    @Override
    public void open() {
        // TODO Auto-generated method stub
        
    }
    
}
