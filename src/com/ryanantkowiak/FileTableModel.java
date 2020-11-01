package com.ryanantkowiak;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FileTableModel extends TableModelWithListeners
{
    private static DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#.00");
    
    private List<File> m_files;
    
    private String m_fromRegex;
    private String m_toRegex;
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public void setRegex(String from, String to)
    {
        m_fromRegex = from;
        m_toRegex = to;
        notifyTableListeners();
    }
        
    public void setFiles(List<File> files)
    {
        m_files = files;
        notifyTableListeners();
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public FileTableModel(List<String> columnNames, List<Class<?>> columnClasses)
    {
        super(columnNames, columnClasses);
    }

    @Override
    public int getRowCount()
    {
        if (m_files == null)
            return 0;
        return m_files.size();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        try
        {
            if (m_files != null && rowIndex < m_files.size())
            {
                File file = m_files.get(rowIndex);
            
                if (file != null)
                {
                    if (columnIndex == 0) // file size
                    {
                        return fileSizeToString(file.length());
                    }
                    else if (columnIndex == 1) // "from" filename
                    {
                        return constructFromFileString(file);
                    }
                    else if (columnIndex == 2) // "to" filename
                    {
                        return constructToFileString(file);
                    }
                }
            }
        }
        catch (Exception e)
        {
        }
        
        return new String();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {        
    }
    
    private static String fileSizeToString(long size)
    {
        if (size >= 1000000000)
        {
            double dSize = ((double)size) / 1000000000.0;
            return DECIMAL_FORMATTER.format(dSize) + " GB";
        }
        else if (size > 1000000)
        {
            double dSize = ((double)size) / 1000000.0;
            return DECIMAL_FORMATTER.format(dSize) + " MB";
        }
        else if (size >= 1000)
        {
            double dSize = ((double)size) / 1000.0;
            return DECIMAL_FORMATTER.format(dSize) + " kB";
        }
        else
        {
            return "" + size + " b";
        }
    }
    
    public List<Triple<String, String, File>> getFileTriples()
    {
        List<Triple<String, String, File>> triples = new ArrayList<>();
        List<String> toFileNames = new ArrayList<String>();
        boolean allToFilesAreUnique = true;
 
        try
        {
            if (m_files != null)
            {
                for (File f : m_files)
                {
                    if (f != null)
                    {
                        try
                        {
                            String from = constructFromFileString(f);
                            String to = constructToFileString(f);
                            File toFileObj = constructToFileObject(f, to);
                            
                            if (from != "" && to != "" && !from.equals(to) && toFileObj != null && !toFileObj.exists())
                            {
                                if (toFileNames.contains(to))
                                {
                                    allToFilesAreUnique = false;
                                }
                                
                                toFileNames.add(to);
                                triples.add(new Triple<String, String, File>(from, to, f));
                            }                            
                        }
                        catch (Exception e)
                        {
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            triples.clear();
        }
        
        if (!allToFilesAreUnique)
        {
            triples.clear();
        }
        
        return triples;
    }
    
    public int getNumTriples()
    {
        List<Triple<String, String, File>> triple = getFileTriples();
        
        if (triple == null)
            return 0;
        return triple.size();
    }
    
    public boolean validateOperation()
    {
        return getNumTriples() > 0;
    }
    
    public File constructToFileObject(File fromFile, String newName)
    {
        if (fromFile == null || newName == null || newName.isEmpty())
            return null;
        
        String parentPath = fromFile.getParent();
        
        if (parentPath == null || parentPath.isEmpty())
            return null;
        
        String path = parentPath + File.separator + newName;
        
        return new File(path);
    }
    
    public void clear()
    {
        if (m_files != null)
        {
            m_files.clear();
        }
    }
    
    private String constructFromFileString(File f)
    {
        try
        {
            if (f != null && f.isFile() && !f.isDirectory())
            {
                return f.getName();
            }
        }
        catch (Exception e)
        {
        }
        
        return "";
    }
    
    private String constructToFileString(File f)
    {
        try
        {
            if (f != null && f.isFile() && !f.isDirectory() && f.exists())
            {
                String s = f.getName();
                if (s.matches(m_fromRegex))
                {
                    s = s.replaceAll(m_fromRegex, m_toRegex);
                    return s;
                }
            }
        }
        catch (Exception e)
        {
        }
        
        return "";
    }
}
