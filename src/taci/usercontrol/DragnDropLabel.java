package taci.usercontrol;

import static taci.usercontrol.ReferenceConstants.CELL_SIZE;
import static taci.usercontrol.ReferenceConstants.DND_LABEL_TOOL_TIP;
import static taci.usercontrol.ReferenceConstants.TACI_CELL_BACK_COLOR;
import static taci.usercontrol.ReferenceConstants.TACI_CELL_BORDER_COLOR;
import static taci.usercontrol.ReferenceConstants.TACI_CELL_OWN_BOARD_FORE_COLOR;

import java.awt.Cursor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

public class DragnDropLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	private static String text = "-1";// The value of this cell

	public DragnDropLabel(String title) {
		super("0".equals(title) ? "" : title);
		
		setHorizontalAlignment(CENTER);
		setVerticalTextPosition(CENTER);
		setBorder(BorderFactory.createLineBorder(TACI_CELL_BORDER_COLOR));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setSize(CELL_SIZE, CELL_SIZE);
		setOpaque(true);
		setBackground(TACI_CELL_BACK_COLOR);
		setForeground(TACI_CELL_OWN_BOARD_FORE_COLOR);
		setLocation(0, 0);
		setTransferHandler(th);
		setToolTipText(DND_LABEL_TOOL_TIP);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JComponent comp = (JComponent) e.getSource();
				TransferHandler th = comp.getTransferHandler();
				th.exportAsDrag(comp, e, TransferHandler.COPY);
			}
		});
	}
	
	public Integer getValue() {
		return Integer.parseInt(getText());
	}
	
	private TransferHandler th = new TransferHandler("text") {
		private static final long serialVersionUID = 1L;

		@Override
		public void exportAsDrag(JComponent comp, InputEvent e, int action) {
			super.exportAsDrag(comp, e, action);
		}

		@Override
		public boolean importData(JComponent comp, Transferable t) {
			text = ((JLabel)comp).getText();
			return super.importData(comp, t);
		}

		@Override
		protected void exportDone(JComponent source, Transferable data,
				int action) {
			if(!text.equals("-1")) {
				setText(text);
			}
			text = "-1";
			super.exportDone(source, data, action);
		}
	};
}
