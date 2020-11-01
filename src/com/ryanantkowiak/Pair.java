package com.ryanantkowiak;

public class Pair<K, V>
{
    public K key;
    public V value;
    
    public Pair(K k, V v)
    {
        key = k;
        value = v;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o == null)
            return false;
        
        try
        {
            Pair<?, ?> rhs = (Pair<?, ?>) o;
            return key.equals(rhs.key) && value.equals(rhs.value);
        }
        catch (Exception e)
        {            
        }
        
        return false;
    }
}
