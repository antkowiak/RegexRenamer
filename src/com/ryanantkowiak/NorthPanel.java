package com.ryanantkowiak;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NorthPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private static String DEFAULT_FROM = "^(.*)$";
    private static String DEFAULT_TO = "$1";
    
    private RegexRenamerFrame m_parent;
    private FileTableModel m_tableModel;
    
    private GridLayout m_layout = new GridLayout(3, 2);

    private JButton m_btnPerformRename = new JButton("Perform Rename");
    private JButton m_btnRefreshDir = new JButton("Refresh Directory");
    private JButton m_btnChooseDir = new JButton("Choose Directory");
    
    private JTextField m_txtDir = new JTextField();
    private JTextField m_txtFrom = new JTextField(DEFAULT_FROM);
    private JTextField m_txtTo = new JTextField(DEFAULT_TO);

    private File m_currentDir;
    
    public NorthPanel(RegexRenamerFrame parent, FileTableModel tableModel)
    {
        m_parent = parent;
        m_tableModel = tableModel;
        
        setVisible(false);
        setLayout(m_layout);

        ///////////////////////////////////////////////////////////////////////////
        m_btnPerformRename.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                onPerformClicked();
            }
        });
        add(m_btnPerformRename);
        ///////////////////////////////////////////////////////////////////////////
        m_btnRefreshDir.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                onRefreshDirClicked();
            }
        });
        add(m_btnRefreshDir);        
        ///////////////////////////////////////////////////////////////////////////
        m_btnChooseDir.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                onChooseDirectoryClicked();
            }
        });
        add(m_btnChooseDir);
        ///////////////////////////////////////////////////////////////////////////
        m_txtDir.setEditable(false);
        add(m_txtDir);
        ///////////////////////////////////////////////////////////////////////////
        m_txtFrom.setEditable(true);
        m_txtFrom.addKeyListener(new KeyListener()
        {
            @Override
            public void keyPressed(KeyEvent event)
            {
                onRegexUpdated();
            }

            @Override
            public void keyTyped(KeyEvent e)
            {
                onRegexUpdated();
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                onRegexUpdated();
            }
        });
        add(m_txtFrom);
        ///////////////////////////////////////////////////////////////////////////
        m_txtTo.setEditable(true);
        m_txtTo.addKeyListener(new KeyListener()
        {
            @Override
            public void keyPressed(KeyEvent event)
            {
                onRegexUpdated();
            }

            @Override
            public void keyTyped(KeyEvent e)
            {
                onRegexUpdated();
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                onRegexUpdated();
            }
        });
        add(m_txtTo);
        ///////////////////////////////////////////////////////////////////////////

        onRegexUpdated();
        setVisible(true);
    }
    
    public void onChooseDirectoryClicked()
    {
        m_tableModel.setFiles(new ArrayList<File>());
        try
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choose a Directory");
            if (m_txtDir != null)
            {
                chooser.setCurrentDirectory(new File(m_txtDir.getText()));
            }
        
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
            if (chooser.showOpenDialog(m_parent) == JFileChooser.APPROVE_OPTION)
            {
                m_currentDir = chooser.getSelectedFile();
                
                if (m_currentDir != null)
                {
                    onRefreshDirClicked();
                }
            }
        }
        catch (Exception e)
        {
        }
        
        m_btnPerformRename.setEnabled(validateOperation());
    }
    
    public void onRegexUpdated()
    {
        m_tableModel.setRegex(m_txtFrom.getText(), m_txtTo.getText());
        m_btnPerformRename.setEnabled(validateOperation());
    }
    
    public void onPerformClicked()
    {
        if (validateOperation())
        {
            List<Triple<String, String, File>> triples = m_tableModel.getFileTriples();
            if (!triples.isEmpty())
            {
                String confirmStr = "Rename [" + triples.size() + "] files?";
                
                if (JOptionPane.showConfirmDialog(m_parent, confirmStr, "Confirm Rename", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
                {
                    System.out.println("Performing rename operations:");
                    
                    for (Triple<String, String, File> t : triples)
                    {
                        System.out.print("[ATTEMPTING] mv \"" + t.a + "\" \"" + t.b + "\"");

                        File toFile = m_tableModel.constructToFileObject(t.c, t.b);
                        
                        if (toFile == null)
                        {
                            System.out.println(" [ERROR]");
                        }
                        else if (t.c.renameTo(toFile))
                        {
                            System.out.println(" [SUCCESS]");
                        }
                        else
                        {
                            System.out.println(" [FAIL]");
                        }
                    }
                    
                    onRefreshDirClicked();
                }
            }
        }
    }    
    
    private void onRefreshDirClicked()
    {
        if (m_tableModel != null)
        {
            m_tableModel.clear();
        }
        
        if (m_txtDir != null && m_tableModel != null && m_currentDir != null)
        {
            m_txtDir.setText(m_currentDir.getAbsolutePath());
        
            File[] files = m_currentDir.listFiles();

            if (files != null)
            {
                List<File> fileList = new ArrayList<File>();

                for (File f : files)
                {
                    if (f.isFile() && !f.isDirectory())
                    {
                        fileList.add(f);
                    }
                }

                m_tableModel.setFiles(fileList);
            }
        }
        
        if (m_tableModel != null)
        {
            m_tableModel.notifyTableListeners();
        }
    }
    
    private boolean validateOperation()
    {
        return m_tableModel.validateOperation();
    }
}
