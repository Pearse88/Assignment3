import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ColourButtonPanel extends JPanel
{
	private JButton penColourBtn,backGroundColourBtn;
	private JLabel penColourLbl,backGroundColourLbl;
	private Color penColour, backColour;
	private Canvas canvasPanel;
	
	public ColourButtonPanel(Canvas inCanvas)
	{
		canvasPanel = inCanvas;	
		penColourLbl = new JLabel("   ");
		penColourLbl.setOpaque(true);
		penColourLbl.setBackground(canvasPanel.getpenColour());
		penColourLbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		backGroundColourLbl = new JLabel("   ");
		backGroundColourLbl.setOpaque(true);
		backGroundColourLbl.setBackground(canvasPanel.getBackGroundColour());
		backGroundColourLbl.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		
		penColourBtn = new JButton("Choose Pen Colour->");
		penColourBtn.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					setpenColour();
				}
			}
		);
		
		backGroundColourBtn = new JButton("Back Ground Colour->");
		backGroundColourBtn.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					setBackGroundColour();
				}
			}
		);
	
		this.setLayout(new GridLayout(1,4));
		this.add(penColourBtn);
		this.add(penColourLbl);
		this.add(backGroundColourBtn);
		this.add(backGroundColourLbl);
	}
	
	public void setpenColour()
	{
		penColour = JColorChooser.showDialog(null,"Pen Colour",penColour);
		if(penColour!=null)
		{
			penColourLbl.setBackground(penColour);
			canvasPanel.setpenColour(penColour);
		}
	}

	public void setBackGroundColour()
	{
		backColour = JColorChooser.showDialog(null,"BackGround Colour",backColour);
		if(backColour!=null)
		{
			backGroundColourLbl.setBackground(backColour);
			canvasPanel.setBackGroundColour(backColour);
		}
	}
}