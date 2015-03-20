package com.ichmed.trinketeers.util.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import com.ichmed.trinketeers.util.render.TextureLibrary;

public class Editor extends JFrame implements ActionListener
{

	private static final long serialVersionUID = -5209002133921099055L;
	private final String createtexliblabel = "Create texturelibrary...";
	private final String elementeditorconstraints = "ElementEditor";
	private final String entityeditorconstraints = "EntityEditor";
	private final String saveelementslabel = "Save elements";
	private final String saveentityslabel = "Save entitys";
	private final String closelabel = "Exit";
	private final String filelabel = "File";

	private ElementEditor elementeditor;
	private EntityEditor entityeditor;

	private JPanel cp = new JPanel();
	private JTabbedPane cl = new JTabbedPane();

	public Editor()
	{
		JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		elementeditor = new ElementEditor();
		entityeditor = new EntityEditor();
		JViewport vp = new JViewport();
		JMenuBar mb = new JMenuBar();
		JMenuItem createtexlib = new JMenuItem(createtexliblabel);
		JMenu file = new JMenu(filelabel);
		JMenuItem saveelements = new JMenuItem(saveelementslabel);
		JMenuItem saveentitys = new JMenuItem(saveentityslabel);
		JMenuItem close = new JMenuItem(closelabel);
		

		createtexlib.setName(createtexliblabel);
		file.setName(filelabel);

		saveelements.setName(saveelementslabel);
		saveentitys.setName(saveentityslabel);
		close.setName(closelabel);

		mb.add(file);

		file.add(createtexlib);
		file.add(saveelements);
		file.add(saveentitys);
		file.add(close);

		saveelements.addActionListener(this);
		saveentitys.addActionListener(this);
		createtexlib.addActionListener(this);
		close.addActionListener(this);

		this.setJMenuBar(mb);
		this.setTitle("Trinketeers Editor");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		sp.setViewport(vp);
		this.getContentPane().add(sp);
		cp.add(cl);

		cl.add(elementeditor, elementeditorconstraints);
		cl.add(entityeditor, entityeditorconstraints);
		vp.setView(cp);
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof JMenuItem)
		{
			JMenuItem src = (JMenuItem) e.getSource();
//			if (src.getName().equals(entityeditorconstraints) || src.getName().equals(elementeditorconstraints)) cl.show(cp, src.getName());
			if (src.getName().equals(createtexliblabel))
			{
				File f = edittl();
				if (f != null && f.isDirectory() == true)
				{
					List<String[]> textures = new ArrayList<String[]>();
					for (File file : f.listFiles())
					{
						String[] texture = new String[2];
						texture[1] = file.getName().subSequence(0, file.getName().lastIndexOf('.')).toString();
						texture[0] = file.getAbsolutePath();
						textures.add(texture);
					}
					try
					{
						TextureLibrary.generateTextureLibrary(f.getAbsolutePath(), textures);
					} catch (Exception e1)
					{
						e1.printStackTrace();
						JDialog dia = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), e1.getLocalizedMessage());
						dia.setVisible(true);
					}
				}
			}
			if (src.getName().equals(saveelementslabel))
			{
				System.out.println("Saving elements");
			}
			if (src.getName().equals(saveentityslabel))
			{
				System.out.println("Saving entitys");
				entityeditor.saveEntitys();
			}
			if (src.getName().equals(closelabel))
			{
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
		}

	}

	private File edittl()
	{
		JFileChooser file = new JFileChooser();
		file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		file.setSelectedFile(new File(".").getAbsoluteFile());
		file.setFileFilter(new FileFilter()
		{
			@Override
			public boolean accept(File f)
			{
				if (f.isDirectory()) return true;
				return false;
			}

			@Override
			public String getDescription()
			{
				return "Folder";
			}
		});
		file.setMultiSelectionEnabled(false);
		if (file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			return file.getSelectedFile();
		} else
		{
			return null;
		}
	}

}
