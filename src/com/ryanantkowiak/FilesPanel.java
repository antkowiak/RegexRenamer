package com.ryanantkowiak;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class FilesPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private RegexRenamerFrame m_parent;
    private FileTableModel m_tableModel;
    
    private JScrollPane m_scrollPane;
    private JTable m_table;
    
    public FilesPanel(RegexRenamerFrame parent, FileTableModel tableModel)
    {
        m_parent = parent;
        m_tableModel = tableModel;
                    
        m_table = new JTable(m_tableModel);
        m_table.getTableHeader().setReorderingAllowed(false);
        m_scrollPane = new JScrollPane(m_table);
        
        setLayout(new BorderLayout());        
        add(m_scrollPane, BorderLayout.CENTER);
    }
}