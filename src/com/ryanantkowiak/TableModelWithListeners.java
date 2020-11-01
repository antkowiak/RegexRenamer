package com.ryanantkowiak;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public abstract class TableModelWithListeners implements TableModel
{
    private final List<String> m_columnNames;
    private final List<Class<?>> m_columnClasses;

    private List<TableModelListener> m_listeners = new ArrayList<TableModelListener>();

    public TableModelWithListeners(List<String> columnNames, List<Class<?>> columnClasses)
    {
        super();
        m_columnNames = new ArrayList<String>(columnNames);
        m_columnClasses = new ArrayList<Class<?>>(columnClasses);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return m_columnClasses.get(columnIndex);
    }

    @Override
    public int getColumnCount()
    {
        return m_columnNames.size();
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        return m_columnNames.get(columnIndex);
    }


    @Override
    public final void addTableModelListener(TableModelListener listener)
    {
        if (!m_listeners.contains(listener))
        {
            m_listeners.add(listener);
        }
    }

    @Override
    public final void removeTableModelListener(TableModelListener listener)
    {
        if (m_listeners != null)
        {
            m_listeners.remove(listener);
        }
    }

    protected final void notifyTableListeners()
    {
        for (TableModelListener listener : m_listeners)
        {
            listener.tableChanged(new TableModelEvent(this));
        }
    }

    protected final void notifyTableListenersUpdateCell(int rowIndex, int columnIndex)
    {
        for (TableModelListener listener : m_listeners)
        {
            listener.tableChanged(new TableModelEvent(this, rowIndex, rowIndex, columnIndex, TableModelEvent.UPDATE));
        }
    }

    protected final void notifyTableListenersUpdateRow(int rowIndex)
    {
        for (TableModelListener listener : m_listeners)
        {
            listener.tableChanged(
                    new TableModelEvent(this, rowIndex, rowIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
        }
    }

    protected final void notifyTableListenersInsertRow(int rowIndex)
    {
        for (TableModelListener listener : m_listeners)
        {
            listener.tableChanged(
                    new TableModelEvent(this, rowIndex, rowIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
        }
    }

    protected final void notifyTableListenersDeleteRow(int rowIndex)
    {
        for (TableModelListener listener : m_listeners)
        {
            listener.tableChanged(
                    new TableModelEvent(this, rowIndex, rowIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
        }
    }
}
