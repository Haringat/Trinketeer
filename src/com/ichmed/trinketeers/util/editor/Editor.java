package com.ichmed.trinketeers.util.editor;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;

import com.ichmed.trinketeers.util.render.TextureLibrary;

public class Editor extends JFrame implements ActionListener, MenuListener{
	
	private static final long serialVersionUID = -5209002133921099055L;
	private final String createtexliblabel = "create texturelibrary";
	private final String elementeditorconstraints = "ElementEditor";
	private final String entityeditorconstraints = "EntityEditor";

	private JPanel cp = new JPanel();
	private CardLayout cl = new CardLayout();

	public Editor() {
		JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JViewport vp = new JViewport();
		JMenuBar mb = new JMenuBar();
		JMenu show = new JMenu("show");
		JMenu createtexlib = new JMenu(createtexliblabel);
		JMenuItem showelementeditor = new JMenuItem(elementeditorconstraints);
		JMenuItem showentityeditor = new JMenuItem(entityeditorconstraints);
		show.add(showelementeditor);
		show.add(showentityeditor);
		showelementeditor.addActionListener(this);
		showentityeditor.addActionListener(this);
		createtexlib.addMenuListener(this);
		//createtexlib.addActionListener(this);
		mb.add(show);
		mb.add(createtexlib);
		this.setJMenuBar(mb);
		vp.setOpaque(false);
		this.setTitle("Trinketeers Editor");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		sp.setViewport(vp);
		this.getContentPane().add(sp);
		cp.setLayout(cl);
		
		cp.add(new ElementEditor(), elementeditorconstraints);
		cp.add(new EntityEditor(), entityeditorconstraints);
		cl.first(cp);
		vp.setView(cp);
		if(vp.getView()!= null){
			System.out.println(vp.getView().toString());
		}
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JMenuItem){
			JMenuItem src = (JMenuItem) e.getSource();
			cl.show(cp, src.getText());
		}
		
	}

	@Override
	public void menuSelected(MenuEvent e) {
		if(e.getSource() instanceof JMenu){
			JMenu src = (JMenu) e.getSource();
			if(src.getText().equals(createtexliblabel)){
				System.out.println("creating texlib");
				File f = edittl();
				if(f != null && f.isDirectory() == true){
					List<String[]> textures = new ArrayList<String[]>();
					for( File file: f.listFiles()){
						String[] texture = new String[2];
						texture[1] = file.getName().subSequence(0, file.getName().lastIndexOf('.')).toString();
						texture[0] = file.getAbsolutePath();
						textures.add(texture);
					}
					try {
						TextureLibrary.generateTextureLibrary(f.getAbsolutePath(), textures);
					} catch (Exception e1) {
						e1.printStackTrace();
						JDialog dia = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), e1.getLocalizedMessage());
						dia.setVisible(true);
					}
				}
			}
		}
		
	}

	@Override
	public void menuDeselected(MenuEvent e) {}

	@Override
	public void menuCanceled(MenuEvent e) {}
	
	private File edittl() {
		JFileChooser file = new JFileChooser();
		file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		file.setSelectedFile(new File(".").getAbsoluteFile());
		file.setFileFilter(new FileFilter(){
			@Override
			public boolean accept(File f) {
				if(f.isDirectory())
					return true;
				return false;
			}
			@Override
			public String getDescription() {
				return "Folder";
			}
		});
		file.setMultiSelectionEnabled(false);
		if(file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			return file.getSelectedFile();
		} else {
			return null;
		}
	}

}
