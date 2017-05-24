package midi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.Vector;

import javax.swing.JPanel;

class Piano extends JPanel implements MouseListener {

        Vector keys = new Vector();
		Vector whiteKeys = new Vector();
		Vector blackKeys = new Vector();
        Key prevKey;
        java.util.Vector permanents = new Vector();
        final int kw = 18, kh = 128;
		final Color jfcBlue = new Color(204, 204, 255);
		final Color pink = new Color(255, 175, 175);
		boolean record;
		private MidiApp midiApp;
    
       public Piano(MidiApp midiApp) {
        	this.midiApp = midiApp;
