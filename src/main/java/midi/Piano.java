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

/*
Piano renders black & white keys and plays the notes for a MIDI
channel.
*/

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

//for some reason this does some stupid crap where it decides to sometimes reformat mid song - needs to be fixed still
		
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(42*kw, kh+1));
            int transpose = 36;
            int whiteIDs[] = { 0, 2, 4, 5, 7, 9, 11 };

            for (int i = 0, x = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++, x += kw) {
                    int keyNum = i * 12 + whiteIDs[j] + transpose;
                    whiteKeys.add(new Key(midiApp, x, 0, kw, kh, keyNum, false));
                  
			/* 
			if (j == 0) {
						Key C = (Key)whiteKeys.elementAt(whiteKeys.size()-1);
						this.getGraphics().setColor(Color.black);
						this.getGraphics().drawString(("c"+new Integer(i).toString()), x+2, 100);
					}*/
			
                }
            }
            for (int i = 0, x = 0; i < 6; i++, x += kw) {
                int keyNum = i * 12 + transpose;
                blackKeys.add(new Key(midiApp, (x += kw)-4, 0, kw/2, kh/2, keyNum+1, true));
                blackKeys.add(new Key(midiApp, (x += kw)-4, 0, kw/2, kh/2, keyNum+3, true));
                x += kw;
                blackKeys.add(new Key(midiApp, (x += kw)-4, 0, kw/2, kh/2, keyNum+6, true));
                blackKeys.add(new Key(midiApp, (x += kw)-4, 0, kw/2, kh/2, keyNum+8, true));
                blackKeys.add(new Key(midiApp, (x += kw)-4, 0, kw/2, kh/2, keyNum+10, true));
            }
            keys.addAll(blackKeys);
            keys.addAll(whiteKeys);
		
//listener used to prevent any stupid bullshit happening that is the equivalent of ghosting on a piano
		
            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseMoved(MouseEvent e) {
                    if (false) {
                        Key key = getKey(e.getPoint());
                        if (prevKey != null && prevKey != key) {
                            prevKey.off();
                        }
                        if (key != null && prevKey != key) {
                            key.on(e.getY());
                        }
                        prevKey = key;
                        repaint();
                    }
                }
            });
            addMouseListener(this);
        }

//simple check for if keys are pressed or not
	    
        public void mousePressed(MouseEvent e) {
            prevKey = getKey(e.getPoint());

            for (int i = 0; i < permanents.size(); i++) {
				if (permanents.elementAt(i).equals(prevKey)) {
				prevKey.off();
                repaint();
                permanents.removeElementAt(i);
                break;
				}
			}
            if (prevKey != null) {
				prevKey.on(e.getY());
                repaint();
            }
        }
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
				permanents.add(prevKey);
				return;
			}
            if (prevKey != null) {
                prevKey.off();
                repaint();
                //prevKey = null;
            }
        }
        public void mouseExited(MouseEvent e) {
		
           /* 
	   if (prevKey != null) {
                prevKey.off();
                repaint();
                prevKey = null;
            }
	    */
		
        }
        public void mouseClicked(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }


        public Key getKey(Point point) {
            for (int i = 0; i < keys.size(); i++) {
                if (((Key) keys.get(i)).contains(point)) {
                    return (Key) keys.get(i);
                }
            }
            return null;
        }

        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Dimension d = getSize();

            g2.setBackground(getBackground());
            g2.clearRect(0, 0, d.width, d.height);

            g2.setColor(Color.white);
            g2.fillRect(0, 0, 42*kw, kh);

//if no stupid shit happens with things canceling out for retarded reasons this should make the keys turn pink
//(it's actually more of a red) when they are being pressed
		
            for (int i = 0; i < whiteKeys.size(); i++) {
                Key key = (Key) whiteKeys.get(i);
                if (key.isNoteOn()) {
                    g2.setColor(record ? pink : jfcBlue);
                    g2.fill(key);
                }
                g2.setColor(Color.black);
                g2.draw(key);
            }
		
/*
hopefully the key doesn't stay pink because it doesn't know to refer to the if, else, and/or for statements
if it does then just make a stupidly-long if, else, and/or for statement that is all connected
can't be bothered to fix it if it doesn't work - will just do the above
*/		
		
            for (int i = 0; i < blackKeys.size(); i++) {
                Key key = (Key) blackKeys.get(i);
                if (key.isNoteOn()) {
                    g2.setColor(record ? pink : jfcBlue);
                    g2.fill(key);
                    g2.setColor(Color.black);
                    g2.draw(key);
                } else {
                    g2.setColor(Color.black);
                    g2.fill(key);
                }
            }
        }
    }

// End class Piano
