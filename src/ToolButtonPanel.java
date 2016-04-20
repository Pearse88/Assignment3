import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ToolButtonPanel extends JPanel
{
	private JButton lineBtn, squareBtn, ovalBtn, polygonBtn, roundedRectBtn, freeHandBtn, undoBtn, redoBtn, clearBtn;		
	
	//check-box to fill shapes
	private JCheckBox fillChk;
	private Canvas canvasPanel;
	
	//tool bar for shapes
	public ToolButtonPanel(Canvas inCanvas)
	{
		canvasPanel = inCanvas;
		 
		//load images as buttons
		lineBtn			= new JButton("",new ImageIcon("images/lineBtn.gif"));
		squareBtn		= new JButton("",new ImageIcon("images/squareBtn.gif"));
		ovalBtn	 		= new JButton("",new ImageIcon("images/ovalBtn.gif"));
		polygonBtn		= new JButton("",new ImageIcon("images/polygonBtn.gif"));
		roundedRectBtn	= new JButton("",new ImageIcon("images/roundedRectBtn.gif"));
		freeHandBtn		= new JButton("",new ImageIcon("images/freeHandBtn.gif"));
		undoBtn			= new JButton("",new ImageIcon("images/undoBtn.gif"));
		redoBtn			= new JButton("",new ImageIcon("images/redoBtn.gif"));
		clearBtn		= new JButton("",new ImageIcon("images/clearBtn.gif"));
		
		lineBtn.addActionListener(new ToolButtonListener());
		lineBtn.setToolTipText("Line");
		
		squareBtn.addActionListener(new ToolButtonListener());
		squareBtn.setToolTipText("Retangle");
		
		ovalBtn.addActionListener(new ToolButtonListener());
		ovalBtn.setToolTipText("Oval");
		
		polygonBtn.addActionListener(new ToolButtonListener());
		polygonBtn.setToolTipText("Polygon");
		
		roundedRectBtn.addActionListener(new ToolButtonListener());
		roundedRectBtn.setToolTipText("Rectangle");
		
		freeHandBtn.addActionListener(new ToolButtonListener());
		freeHandBtn.setToolTipText("Free Hand");
		
		undoBtn.addActionListener(new ToolButtonListener());
		undoBtn.setToolTipText("Undo");
		
		redoBtn.addActionListener(new ToolButtonListener());
		redoBtn.setToolTipText("Redo");
		
		clearBtn.addActionListener(new ToolButtonListener());
		clearBtn.setToolTipText("Clear Canvas");

		
		fillChk = new JCheckBox("fill");
		fillChk.addItemListener(
			new ItemListener()
			{
				public void itemStateChanged(ItemEvent event)
				{
					if(fillChk.isSelected())
						canvasPanel.setSolidMode(Boolean.TRUE);
					else
						canvasPanel.setSolidMode(Boolean.FALSE);
				}	
			}
		);// end addItemListener
		
		// 8 Buttons & 1 CheckBox
		this.setLayout(new GridLayout(1,9)); 
		this.add(lineBtn);
		this.add(squareBtn);
		this.add(ovalBtn);
		this.add(polygonBtn);
		this.add(roundedRectBtn);
		this.add(freeHandBtn);
		this.add(undoBtn);
		this.add(redoBtn);
		this.add(clearBtn);
		this.add(fillChk);				
	}

	private class ToolButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{	
			if(canvasPanel.isExistPolygonBuffer()!= false)
			{
				canvasPanel.flushPolygonBuffer();
			}
			if(event.getSource() == lineBtn)
			{
				canvasPanel.setDrawMode(Canvas.LINE);		
			}
			if(event.getSource() == squareBtn)
			{
				canvasPanel.setDrawMode(Canvas.SQUARE);
			}
			if(event.getSource() == ovalBtn)
			{
				canvasPanel.setDrawMode(Canvas.CIRCLE);
			}
			if(event.getSource() == polygonBtn)
			{
				canvasPanel.setDrawMode(Canvas.POLYGON);
			}
			if(event.getSource() == roundedRectBtn)
			{
				canvasPanel.setDrawMode(Canvas.ROUNDED_RECT);
			}
			if(event.getSource() == freeHandBtn)
			{
				canvasPanel.setDrawMode(Canvas.FREE_HAND);
			}
			if(event.getSource() == undoBtn)
			{
				canvasPanel.undo();
			}
			if(event.getSource() == redoBtn)
			{
				canvasPanel.redo();
			}
			if(event.getSource() == clearBtn)
			{
				canvasPanel.clearCanvas();
			}
		}
	}
}
