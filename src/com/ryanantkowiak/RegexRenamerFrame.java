package com.ryanantkowiak;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

public class RegexRenamerFrame extends JFrame
{
    private static final long serialVersionUID = 1L;

    private FileTableModel m_tableModel;
    
    private BorderLayout m_outterLayout = new BorderLayout();
    
    private NorthPanel m_northPanel;
    private FilesPanel m_filesPanel;
    
    public RegexRenamerFrame() throws HeadlessException
    {
        setVisible(false);
        
        List<String> columns = Arrays.asList("Size", "Original Name", "New Name");
        List<Class<?>> types = Arrays.asList(String.class, String.class, String.class);
        m_tableModel = new FileTableModel(columns, types);
        
        m_northPanel = new NorthPanel(this, m_tableModel);
        m_filesPanel = new FilesPanel(this, m_tableModel);
        
        setTitle("Regex Renamer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(m_outterLayout);
        getContentPane().add(m_northPanel, BorderLayout.NORTH);
        getContentPane().add(m_filesPanel, BorderLayout.CENTER);
    }

}
