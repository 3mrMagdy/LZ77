import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;

class tag
{
	int st, len;
	char nxtChar;
}

public class Run 
{
	public static void main (String arg[])
	{
		new Run().Gui(); 
	}
	
	void Gui()
	{
		JFrame frm = new JFrame ("LZW");
		frm.setSize(500,300);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel pnl = new JPanel();
		frm.add(pnl);
		
		pnl.setLayout(null);
		
		JLabel l = new JLabel("File path");
		l.setBounds(211, 51, 71, 31);
		pnl.add(l);
		
		JTextField tf = new JTextField();
		tf.setBounds(99, 91, 277, 33);
		pnl.add(tf);
		
		JButton b1 = new JButton("Compress");
		b1.setBounds(33, 177, 111, 51);
		pnl.add(b1);
		
		JButton b2 = new JButton("Decompress");
		b2.setBounds(333, 177, 111, 51);
		pnl.add(b2);
		
		b1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Comp(tf.getText());
				JOptionPane.showMessageDialog(null, "Done!!");
			}
		});
		
		b2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Decomp(tf.getText());
				JOptionPane.showMessageDialog(null, "Done!!");
			}
		});

		frm.setVisible(true);
		
	}
	
	void Comp (String path)
	{
		int cnt=0, mx=0, st=0;
		String doc = new Run().read_from_dataFile(path);
		tag tmp = new tag();
		ArrayList <tag> tags = new ArrayList <tag>();
		
		for(int i=0 ; i<doc.length() ; i+=mx+1)
		{
			mx=0;
			for(int j=0 ; j<i ; j++)
			{
				cnt=0;
				
				for(int k=0 ; k+i<doc.length() && doc.charAt(k+j)==doc.charAt(k+i) ; k++,cnt++);
				
				if(cnt>=mx)
				{
					mx=cnt;
					st=j;
				}
			}
			
			if (i+mx==doc.length())
				mx--;
			
			tmp.st = st;
			tmp.len = mx;
			tmp.nxtChar = doc.charAt(i+mx);
			
			tags.add(tmp);
			tmp = new tag();
		}
		
		new Run().write_to_comFile(tags);
	}
	
	void write_to_comFile (ArrayList <tag> tags)
	{
		File comFile = new File ("com.txt");
	
		try 
		{
			comFile.createNewFile();
			FileWriter writer = new FileWriter(comFile);
			
			for(tag x : tags)
			{
				writer.write(x.st + " ");
				writer.write(x.len + " ");
				writer.write(x.nxtChar + " ");
			}
			
			writer.close();
		}
		catch(IOException ex)
		{
			JOptionPane.showMessageDialog(null, "Path is invalid");
		}
	}

	String read_from_dataFile (String fileName)
	{
		String doc="", tmp;
		try
		{
			Scanner in = new Scanner (new File (fileName));

			while (in.hasNext())
			{
				tmp = in.next();
				doc+=tmp;
			}
			
			in.close();
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Path is invalid");
		}
		
		return doc;
	}

	void Decomp (String path)
	{
		ArrayList <tag> tags = new Run().read_from_comFile(path);
		String doc="";
		
		for(tag tmp : tags)
		{
			for(int i=0 ; i<tmp.len ; i++)
				doc+=doc.charAt(i+tmp.st);
			doc+=tmp.nxtChar;
		}
		
		new Run().write_to_dataFile(doc);
	}
	
	ArrayList <tag> read_from_comFile (String path)
	{
		tag tmp = new tag();
		ArrayList <tag> tags = new ArrayList <tag> ();
		
		try
		{
			Scanner in = new Scanner (new File (path));
			
			while (in.hasNext())
			{
				tmp.st = in.nextInt();
				tmp.len = in.nextInt();
				tmp.nxtChar = in.next().charAt(0);
				tags.add(tmp);
				tmp = new tag();
			}
			
			in.close();
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Path is invalid");
			System.exit(0);
		}
		
		return tags;
	}

	void write_to_dataFile (String doc)
	{
		File dataFile = new File ("newdata.txt");
		
		try
		{
			dataFile.createNewFile();
			
			FileWriter writer = new FileWriter (dataFile);
			
			writer.write(doc);
			
			writer.close();
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Path is invalid");
			System.exit(0);
		}
	}
}