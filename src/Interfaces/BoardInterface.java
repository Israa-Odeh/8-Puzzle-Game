/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import static Interfaces.StartInterface.algorithm;
import static Interfaces.StartInterface.goal;
import static Interfaces.StartInterface.heuristic;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Image;
import java.awt.Insets;
import static java.lang.Character.isDigit;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 *
 * @author User
 */
public class BoardInterface extends javax.swing.JFrame {

    /**
     * Creates new form UDPPeer1
     */
    final static int goal1 [][] = { {1, 2, 3}, {4, 5, 6}, {7, 8, 0} };
    final static int goal2 [][] = { {1, 2, 3}, {8, 0, 4}, {7, 6, 5} };
    int startPt [][] = new int [3][3];
    State startNode;
    static int pathCount; /////edited
    static int heuristicID;
    static int myGoal [][];
    final static ArrayList<String> tieBreaker = new ArrayList<String> ();
        
    public BoardInterface() {
        tieBreaker.add("up");
	tieBreaker.add("left");
	tieBreaker.add("right");
	tieBreaker.add("down");
        initComponents();
        setLocationRelativeTo(null);
        
        jScrollPane_SolutionPath.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane_SolutionPath.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }
    
    private static int calculateH(State state) {
	int h = 0;
	if(heuristicID == 1) {
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				if(state.board[i][j] != myGoal[i][j]) {
					if(state.board[i][j] == 0) continue;
					h++;
				}
			}
		}
	}
		
	else if(heuristicID == 2) {
		boolean found = false;
		for(int x1=0; x1<3; x1++) {
			for(int y1=0; y1<3; y1++) {
				if(state.board[x1][y1] != myGoal[x1][y1]) {
					if(state.board[x1][y1] == 0) continue;
					found = false;
					for(int x2=0; x2<3; x2++) {
						for(int y2=0; y2<3; y2++) {
							if(state.board[x1][y1] == myGoal[x2][y2]) {
								h += Math.abs(x1-x2) + Math.abs(y1-y2);
								found = true;
								break;
							}
						}
						if (found) break;
					}
				}
			}
		}
	}
	return h;
    }
    
    public static ArrayList<State> greadySearch(State startNode) {
		//the parent of the startNode should equal null
		ArrayList <State> closedList = new ArrayList<>();
		ArrayList <State> openList = new ArrayList<>();
		openList.add(startNode);
		State n;
                int counter = 0;                                                 //******
                System.out.println("start loop");                                //******
		do {
			n = getBestH(openList);
                        System.out.println("Test Node #" + counter++);
                        showTestedNode(n);
                        //if (counter != 1) System.out.println("best: " + n.func + n.cost + " parent: " + n.parent.func + n.parent.cost + " h = " + n.heurestic);                                //******
			if (isGoal(n)){
                            //showTestedNodes(closedList);
                            return getPath(n);
                        }
			ArrayList<State> children = getChildren(n);
			for (State child : children) {
				if(!isExist(child, openList) && !isExist(child, closedList)) {
					child.parent = n;
					openList.add(child);
				}
			}
                        closedList.add(n);
			openList.remove(n);
			
//                        System.out.print("open list: ");
//                        for (State child3 : openList) {
//                            System.out.print(child3.func + child3.cost + "   ");
//                        }
//                        System.out.println("");
//                        System.out.print("closed list: ");
//                        for (State child4 : closedList) {
//                            System.out.print(child4.func + child4.cost + "   ");
//                        }
//                        System.out.println("");
//                        System.out.println("");
//                        System.out.println("");
		}while(!openList.isEmpty());
		return null;
	}
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static ArrayList<State> aStarSearch(State startNode) {
		//the parent of the startNode should equal null
		ArrayList <State> closedList = new ArrayList<>();
		ArrayList <State> openList = new ArrayList<>();
		openList.add(startNode);
		State n;
                int counter = 0;                                                 //******
                System.out.println("start loop");                                //******
		do {
			n = getBestF(openList);
                        System.out.println("Test Node #" + counter++);
                        showTestedNode(n);
                        //if (counter != 1) System.out.println("best: " + n.func + n.cost + " parent: " + n.parent.func + n.parent.cost + " h = " + n.heurestic);                                //******
			if (isGoal(n)){
                            //showTestedNodes(closedList);
                            return getPath(n);
                        }
			ArrayList<State> children = getChildren(n);
			for (State child : children) {
				if(!isExist(child, openList) && !isExist(child, closedList)) {
					child.parent = n;
					openList.add(child);
				}
                                //Updated.
                                else {
                                    State checkedNode = null;
                                    if (isExist(child, openList)) {
                                        for (int i = 0; i < openList.size(); i++) {
                                            if (isBoardEquals(child, openList.get(i))) {
                                                checkedNode = openList.get(i);
                                                if (child.f < checkedNode.f) {
                                                    openList.remove(checkedNode);
                                                    openList.add(child);
                                                }
                                                break;
                                            }
                                        }
                                    } 
                                    else {
                                        //Find the node that is the same as the child node in the closed list.
                                        for (int i = 0; i < closedList.size(); i++) {
                                            if (isBoardEquals(child, closedList.get(i))) {
                                                checkedNode = closedList.get(i);
                                                if (child.f < checkedNode.f) {
                                                    checkedNode.parent = child.parent;
                                                    checkedNode.cost = child.cost;
                                                    checkedNode.f = checkedNode.cost + checkedNode.heurestic;
                                                    openList.add(closedList.remove(i));
                                                }
                                                break;
                                            }
                                        }
                                    }
                }

            }
            closedList.add(n);
            openList.remove(n);

        } while (!openList.isEmpty());
        return null;
    }
    
    private static State getBestF(ArrayList<State> openList) {
        //if (openList.isEmpty()) return null;
        State best = openList.get(0);
        for (State state : openList) {
            if (state.f < best.f) {
                best = state;
            }
            else if (state.f == best.f) {
                if(state.heurestic < best.heurestic) {
                    best = state;
                }
                else if(state.heurestic == best.heurestic) {
                    if (tieBreaker.indexOf(state.func) < tieBreaker.indexOf(best.func)) {
                        best = state;
                    }
                }
            }
        }
        return best;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private static void showTestedNode(State n) {
        for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
                            if(n.board[i][j] == 0 )System.out.print("   ");
                            else System.out.print(n.board[i][j] + "  ");
                        }
                        System.out.print("\n");
             }

    }

	private static State getBestH (ArrayList<State> openList) {
		//if (openList.isEmpty()) return null;
		State best = openList.get(0);
		for(State state : openList) {
			if(state.heurestic < best.heurestic) best = state;
			else if(state.heurestic == best.heurestic) {
				if(tieBreaker.indexOf(state.func) < tieBreaker.indexOf(best.func)) best = state;
			}
		}
		return best;
	} 
	
	private static boolean isGoal(State n) {
		for(int i=0; i<3; i++)
			for(int j=0; j<3; j++) {
				if(n.board[i][j] != myGoal[i][j]) return false;
			}
		return true;
	}
        
	private static ArrayList<State> getPath(State goal){
		ArrayList<State> path = new ArrayList<>();
                ArrayList<State> reversePath = new ArrayList<>();
		pathCount = 0;// it was int pathCount = 0. I'll move it up.
		State s = goal;
		while(s.parent != null) {
			pathCount++;
                        reversePath.add(s);
			s = s.parent;
		}
                reversePath.add(s);
                //System.out.println("pathcount" + pathCount); //******
		while(pathCount >= 0) {
			path.add(reversePath.remove(pathCount));              //indexOutOfBound exception
			pathCount--;
		}
		return path;
	}

	private static ArrayList<State> getChildren(State n) {
		int x = 0, y = 0;
		State child;
		ArrayList<State> children = new ArrayList<>();
		
		//find blank position
		for(int i=0; i<3; i++)
			for(int j=0; j<3; j++)
				if(n.board[i][j] == 0) {
					x = i;
					y = j;
				}
		/* we can declare a function to extract a new child and add it to the children
		we will give this function the parent, func, and x & y offsets*/
		
		if(x != 0) { //if blank.x != 0 then can go up
			child = new State(n.board, n, "up", n.cost + 1, n.heurestic);
			child.board[x][y] = child.board[x-1][y];
			child.board[x-1][y] = 0;
			child.heurestic = calculateH(child); //recalculate h
                        child.cost = n.cost + 1;        //edited.
                        child.f = child.heurestic + child.cost; //edited.
			children.add(child);
		}
		
		if(x != 2) { //if blank.x != 2 then can go down
			child = new State(n.board, n, "down", n.cost + 1, n.heurestic);
			child.board[x][y] = child.board[x+1][y];
			child.board[x+1][y] = 0;
			child.heurestic = calculateH(child); //recalculate h
                        child.cost = n.cost + 1;        //edited.
                        child.f = child.heurestic + child.cost; //edited.
			children.add(child);
		}
		
		if(y != 0) { //if blank.y != 0 then can go left
			child = new State(n.board, n, "left", n.cost + 1, n.heurestic);
			child.board[x][y] = child.board[x][y-1];
			child.board[x][y-1] = 0;
			child.heurestic = calculateH(child); //recalculate h
                        child.cost = n.cost + 1;        //edited.
                        child.f = child.heurestic + child.cost; //edited.
			children.add(child);
		}
		
		if(y != 2) { //if bland.y != 2 then can go right
			child = new State(n.board, n, "right", n.cost + 1, n.heurestic);
			child.board[x][y] = child.board[x][y+1];
			child.board[x][y+1] = 0;
			child.heurestic = calculateH(child); //recalculate h
                        child.cost = n.cost + 1;        //edited.
                        child.f = child.heurestic + child.cost; //edited.
			children.add(child);
		}
//                for(State child2 : children){
//                    System.out.println("new child : moved " + child2.func + child2.cost);                                //******
//                }
		return children;
	}
        
        private static boolean isExist(State child, ArrayList<State> list) {
		for(State child1 : list) {
			if(isBoardEquals(child, child1))  return true;
		}
		return false;
	}

	private static boolean isBoardEquals(State child, State child1) {
		for(int i=0; i<3; i++)
			for(int j=0; j<3; j++)
				if(child.board[i][j] != child1.board[i][j]) return false;
				return true;
	}
    
    //This function is a timer that specifies the time of showing the notification.
    private void callTimer(JFrame notification) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                notification.setVisible(false);
            }
        }, 6000);
    }
    
    //This function receives a message and displays it on a notification.
    private void displayNotification(String message) {
        Notification error = new Notification();
        error.setText(message);
        error.setVisible(true);
        callTimer(error);
    }

    //Function to replace the button with another one with a different color.
    private void scaleimage__replaceButton(JLabel label, String path) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image imgscale = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imgscale);
        label.setIcon(scaledIcon);
    }
    
    //A Function to make the label's background white when the mouse is entered.
    public void scaleimage__backcwhite(JLabel label, String path) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image imgscale = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imgscale);
        label.setIcon(scaledIcon);
    }

    //A Function to return the label's original background when the mouse is exited.
    public void scaleimage__backc_white_remove(JLabel label) {
        ImageIcon icon = new ImageIcon("");
        Image img = icon.getImage();
        Image imgscale = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imgscale);
       label.setIcon(scaledIcon);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLbl_Logo = new javax.swing.JLabel();
        jLbl_Title = new javax.swing.JLabel();
        jLbl_UpBar = new javax.swing.JLabel();
        jTxt_1 = new javax.swing.JTextField();
        jTxt_2 = new javax.swing.JTextField();
        jTxt_3 = new javax.swing.JTextField();
        jTxt_4 = new javax.swing.JTextField();
        jTxt_5 = new javax.swing.JTextField();
        jTxt_6 = new javax.swing.JTextField();
        jTxt_7 = new javax.swing.JTextField();
        jTxt_8 = new javax.swing.JTextField();
        jTxt_9 = new javax.swing.JTextField();
        jLbl_Container = new javax.swing.JLabel();
        jLbl_GenerateSolution = new javax.swing.JLabel();
        jLbl_Return_Back = new javax.swing.JLabel();
        jLbl_clear = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane_SolutionPath = new javax.swing.JScrollPane();
        TSolutionPath = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("8-Puzzle Game");
        setIconImage(new ImageIcon("src\\Images\\Logo Mersal.png").getImage());
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLbl_Logo.setFont(new java.awt.Font("Script MT Bold", 1, 17)); // NOI18N
        jLbl_Logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbl_Logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logo.png"))); // NOI18N
        jPanel1.add(jLbl_Logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, 70, 70));

        jLbl_Title.setFont(new java.awt.Font("Century Gothic", 3, 45)); // NOI18N
        jLbl_Title.setForeground(new java.awt.Color(255, 255, 255));
        jLbl_Title.setText("8 - Puzzle");
        jPanel1.add(jLbl_Title, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, 250, 70));

        jLbl_UpBar.setBackground(new java.awt.Color(38, 50, 54));
        jLbl_UpBar.setOpaque(true);
        jPanel1.add(jLbl_UpBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 70));

        jTxt_1.setBackground(new java.awt.Color(245, 245, 245));
        jTxt_1.setFont(new java.awt.Font("Source Sans Pro SemiBold", 0, 48)); // NOI18N
        jTxt_1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTxt_1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jTxt_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxt_1ActionPerformed(evt);
            }
        });
        jPanel1.add(jTxt_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 130, 132, 132));

        jTxt_2.setBackground(new java.awt.Color(245, 245, 245));
        jTxt_2.setFont(new java.awt.Font("Source Sans Pro SemiBold", 0, 48)); // NOI18N
        jTxt_2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTxt_2.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jTxt_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxt_2ActionPerformed(evt);
            }
        });
        jPanel1.add(jTxt_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(179, 130, 132, 132));

        jTxt_3.setBackground(new java.awt.Color(245, 245, 245));
        jTxt_3.setFont(new java.awt.Font("Source Sans Pro SemiBold", 0, 48)); // NOI18N
        jTxt_3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTxt_3.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jPanel1.add(jTxt_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(313, 130, 132, 132));

        jTxt_4.setBackground(new java.awt.Color(245, 245, 245));
        jTxt_4.setFont(new java.awt.Font("Source Sans Pro SemiBold", 0, 48)); // NOI18N
        jTxt_4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTxt_4.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jPanel1.add(jTxt_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 265, 132, 132));

        jTxt_5.setBackground(new java.awt.Color(245, 245, 245));
        jTxt_5.setFont(new java.awt.Font("Source Sans Pro SemiBold", 0, 48)); // NOI18N
        jTxt_5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTxt_5.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jPanel1.add(jTxt_5, new org.netbeans.lib.awtextra.AbsoluteConstraints(179, 265, 132, 132));

        jTxt_6.setBackground(new java.awt.Color(245, 245, 245));
        jTxt_6.setFont(new java.awt.Font("Source Sans Pro SemiBold", 0, 48)); // NOI18N
        jTxt_6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTxt_6.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jTxt_6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxt_6ActionPerformed(evt);
            }
        });
        jPanel1.add(jTxt_6, new org.netbeans.lib.awtextra.AbsoluteConstraints(313, 265, 132, 132));

        jTxt_7.setBackground(new java.awt.Color(245, 245, 245));
        jTxt_7.setFont(new java.awt.Font("Source Sans Pro SemiBold", 0, 48)); // NOI18N
        jTxt_7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTxt_7.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jPanel1.add(jTxt_7, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 400, 132, 132));

        jTxt_8.setBackground(new java.awt.Color(245, 245, 245));
        jTxt_8.setFont(new java.awt.Font("Source Sans Pro SemiBold", 0, 48)); // NOI18N
        jTxt_8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTxt_8.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jPanel1.add(jTxt_8, new org.netbeans.lib.awtextra.AbsoluteConstraints(179, 400, 132, 132));

        jTxt_9.setBackground(new java.awt.Color(245, 245, 245));
        jTxt_9.setFont(new java.awt.Font("Source Sans Pro SemiBold", 0, 48)); // NOI18N
        jTxt_9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTxt_9.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jPanel1.add(jTxt_9, new org.netbeans.lib.awtextra.AbsoluteConstraints(313, 400, 132, 132));

        jLbl_Container.setBackground(new java.awt.Color(229, 229, 229));
        jLbl_Container.setFont(new java.awt.Font("Zilla Slab SemiBold", 0, 24)); // NOI18N
        jLbl_Container.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbl_Container.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(38, 50, 54)));
        jLbl_Container.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLbl_Container.setOpaque(true);
        jPanel1.add(jLbl_Container, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 125, 410, 411));

        jLbl_GenerateSolution.setFont(new java.awt.Font("Zilla Slab SemiBold", 0, 24)); // NOI18N
        jLbl_GenerateSolution.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbl_GenerateSolution.setText("Generate Solution");
        jLbl_GenerateSolution.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(38, 50, 54)));
        jLbl_GenerateSolution.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLbl_GenerateSolution.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLbl_GenerateSolutionMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLbl_GenerateSolutionMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLbl_GenerateSolutionMouseExited(evt);
            }
        });
        jPanel1.add(jLbl_GenerateSolution, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 370, 220, 50));

        jLbl_Return_Back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/BlackBack.png"))); // NOI18N
        jLbl_Return_Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLbl_Return_BackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLbl_Return_BackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLbl_Return_BackMouseExited(evt);
            }
        });
        jPanel1.add(jLbl_Return_Back, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 540, 35, 28));

        jLbl_clear.setFont(new java.awt.Font("Zilla Slab SemiBold", 0, 23)); // NOI18N
        jLbl_clear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbl_clear.setText("Clear Fields");
        jLbl_clear.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(38, 50, 54)));
        jLbl_clear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLbl_clear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLbl_clearMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLbl_clearMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLbl_clearMouseExited(evt);
            }
        });
        jPanel1.add(jLbl_clear, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 450, 220, 50));

        jLabel4.setFont(new java.awt.Font("Segoe Print", 1, 36)); // NOI18N
        jLabel4.setText("Solution Path: ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 130, 430, 40));

        jScrollPane_SolutionPath.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane_SolutionPath.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TSolutionPath.setEditable(false);
        TSolutionPath.setColumns(15);
        TSolutionPath.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        TSolutionPath.setRows(5);
        TSolutionPath.setToolTipText("Chatting Screen");
        TSolutionPath.setWrapStyleWord(true);
        TSolutionPath.setAlignmentX(2.0F);
        TSolutionPath.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(38, 50, 54)));
        TSolutionPath.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane_SolutionPath.setViewportView(TSolutionPath);

        jPanel1.add(jScrollPane_SolutionPath, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 180, 400, 150));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLbl_GenerateSolutionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLbl_GenerateSolutionMouseClicked
        // The squares must be checked for any fillings that are not allowed.
        String enteredNumbers[] = new String[9]; //Define an array of 9 elements (the # of tiles on the borad).
        //Read all the tiles, and store their values in the array.
        enteredNumbers[0] = jTxt_1.getText();
        enteredNumbers[1] = jTxt_2.getText();
        enteredNumbers[2] = jTxt_3.getText();
        enteredNumbers[3] = jTxt_4.getText();
        enteredNumbers[4] = jTxt_5.getText();
        enteredNumbers[5] = jTxt_6.getText();
        enteredNumbers[6] = jTxt_7.getText();
        enteredNumbers[7] = jTxt_8.getText();
        enteredNumbers[8] = jTxt_9.getText();
        
        //Loop through all of the 9 elements to count the number of unfilled tiles (Having a NULL value).
        int count = 0;
        int indexOfNull = -1; //Keep an index of the null (in the case of one empty tile, this would be useful).
        for (int i = 0; i < 9; i++) {
            //For each unfilled tile increment the counter by 1.
            if (enteredNumbers[i].equals("")) {
                indexOfNull = i;
                count++;
            }
        }
        
        //Check if there are more than one unfilled tile (Unallowed).
        if (count > 1) {
            displayNotification("Please make sure that 8 tiles are filled!");
        }
        //Check if all of the 9 tiles are filled (Unallowed).
        else if(count == 0) {
            displayNotification("Please make sure that only 8 tiles are filled!");
        }
        //Otherwise, only 8 tiles will be filled (the allowed case).
        else {
            //Check if the entered values are allowed digits.
            int invalidValueFlag = 0; //A flag indicating whether the tile value is invalid (Non-Digit or out of range value).
            int duplicatedValueFlag = 0; //A flag indicating whether the tile values are duplicated.

            //Loop through all tiles to ensure that they contain digit values.
            for (int i = 0; i < 9; i++) {
                if (i == indexOfNull) {
                    invalidValueFlag = 0;
                }
                else {
                    try {
                        int intValue = Integer.parseInt(enteredNumbers[i]);
                        if (intValue > 0 && intValue < 9) {
                            //System.out.print(enteredNumbers[i] + " :Valid!"); //Code Tracing.
                            invalidValueFlag = 0;
                        }
                        else {
                            //System.out.print(enteredNumbers[i] + " :InValid!"); //Code Tracing.
                            invalidValueFlag = 1;
                        }
                    } 
                    catch (NumberFormatException e) {
                        //System.out.print(enteredNumbers[i] + " :InValid!"); //Code Tracing.
                        invalidValueFlag = 1;
                    }
                }
                if (invalidValueFlag == 1) {
                    displayNotification("Enter valid tile values between 1 and 8!");
                    break;
                }
            }
            //Loop through all tiles to ensure that they aren't duplicated.
            for(int i = 0; i < 9; i++) {
                for(int j = i + 1; j < 9; j++) {
                    if(enteredNumbers[i].equals(enteredNumbers[j])) {
                        displayNotification("Ensure that your values are not duplicated!");
                        duplicatedValueFlag = 1;
                        break;
                    }
                }
            }
            
            //All tile values are allowed, so start coding the game.
            if(invalidValueFlag == 0 && duplicatedValueFlag == 0) {
                //System.out.print("The tile values are valid!"); //Code Tracing.
                int index = 0;
                for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
                            if(enteredNumbers[index].equals("") ) startPt[i][j] = 0;
                            else startPt[i][j] = Integer.parseInt(enteredNumbers[index]);
                            index++;
                        }
                }
                if(StartInterface.heuristic.equals("Heuristic1")) heuristicID = 1;
                else heuristicID = 2;
                if(StartInterface.goal.equals("Row-Major Order")) myGoal = goal1;
                else myGoal = goal2;
                ArrayList<State> solutionPath = new ArrayList<>();
                
                startNode = new State(startPt, null, null, 0, 0);
                startNode.heurestic = calculateH(startNode);
                if(StartInterface.algorithm.equals("Greedy Algorithm")) {
                    
                    solutionPath = greadySearch(startNode);
                    //System.out.println("Yaqout");    
                    //******
                }
                
                else {
                    solutionPath = aStarSearch(startNode);
                    //System.out.println("Yaqout2");  
                }
                //Clear the textArea.
                TSolutionPath.setText("");
                TSolutionPath.setLineWrap(true);
                //Loop through all nodes in the solution path, skip the first node "null", and reverse the function of each one of them.
                int i = 0;
                for (State node : solutionPath) {
                    if (node.func == null) {
                        continue;
                    }
                    else {
                        if (node.func.equals("up")) {
                            TSolutionPath.setText(TSolutionPath.getText() + "down ");
                        } 
                        else if (node.func.equals("right")) {
                            TSolutionPath.setText(TSolutionPath.getText() + "left ");
                        } 
                        else if (node.func.equals("left")) {
                            TSolutionPath.setText(TSolutionPath.getText() + "right ");
                        } 
                        else {
                            TSolutionPath.setText(TSolutionPath.getText() + "up ");
                        }
                    }
                    i++;
                    if(i != solutionPath.size() - 1) TSolutionPath.setText(TSolutionPath.getText() + "-> ");
                    
                }
                
            }
        }
    }//GEN-LAST:event_jLbl_GenerateSolutionMouseClicked

    private void jLbl_GenerateSolutionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLbl_GenerateSolutionMouseEntered
        // TODO add your handling code here:
        scaleimage__backcwhite(jLbl_GenerateSolution, "src\\Images\\Gray.PNG");
        jLbl_GenerateSolution.setForeground(new Color(38,50,54));
    }//GEN-LAST:event_jLbl_GenerateSolutionMouseEntered

    private void jLbl_GenerateSolutionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLbl_GenerateSolutionMouseExited
        // TODO add your handling code here:
        scaleimage__backc_white_remove(jLbl_GenerateSolution);
        jLbl_GenerateSolution.setForeground(Color.black);
    }//GEN-LAST:event_jLbl_GenerateSolutionMouseExited

    private void jLbl_Return_BackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLbl_Return_BackMouseExited
        scaleimage__replaceButton(jLbl_Return_Back, "src\\Images\\BlackBack.png");
    }//GEN-LAST:event_jLbl_Return_BackMouseExited

    private void jLbl_Return_BackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLbl_Return_BackMouseEntered
        scaleimage__backcwhite(jLbl_Return_Back, "src\\Images\\GrayBack.png");
    }//GEN-LAST:event_jLbl_Return_BackMouseEntered

    private void jLbl_Return_BackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLbl_Return_BackMouseClicked
        new StartInterface().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jLbl_Return_BackMouseClicked

    private void jLbl_clearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLbl_clearMouseClicked
        // TODO add your handling code here:
        TSolutionPath.setText("");
        jTxt_1.setText("");
        jTxt_2.setText("");
        jTxt_3.setText("");
        jTxt_4.setText("");
        jTxt_5.setText("");
        jTxt_6.setText("");
        jTxt_7.setText("");
        jTxt_8.setText("");
        jTxt_9.setText("");
    }//GEN-LAST:event_jLbl_clearMouseClicked

    private void jLbl_clearMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLbl_clearMouseEntered
        // TODO add your handling code here:
        scaleimage__backcwhite(jLbl_clear, "src\\Images\\Gray.PNG");
        jLbl_clear.setForeground(new Color(38,50,54));
    }//GEN-LAST:event_jLbl_clearMouseEntered

    private void jLbl_clearMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLbl_clearMouseExited
        // TODO add your handling code here:
        scaleimage__backc_white_remove(jLbl_clear);
        jLbl_clear.setForeground(Color.black);
    }//GEN-LAST:event_jLbl_clearMouseExited

    private void jTxt_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxt_1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTxt_1ActionPerformed

    private void jTxt_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxt_2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTxt_2ActionPerformed

    private void jTxt_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxt_6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTxt_6ActionPerformed
  
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BoardInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BoardInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BoardInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BoardInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BoardInterface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTextArea TSolutionPath;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLbl_Container;
    private javax.swing.JLabel jLbl_GenerateSolution;
    private javax.swing.JLabel jLbl_Logo;
    private javax.swing.JLabel jLbl_Return_Back;
    private javax.swing.JLabel jLbl_Title;
    private javax.swing.JLabel jLbl_UpBar;
    private javax.swing.JLabel jLbl_clear;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane_SolutionPath;
    private javax.swing.JTextField jTxt_1;
    private javax.swing.JTextField jTxt_2;
    private javax.swing.JTextField jTxt_3;
    private javax.swing.JTextField jTxt_4;
    private javax.swing.JTextField jTxt_5;
    private javax.swing.JTextField jTxt_6;
    private javax.swing.JTextField jTxt_7;
    private javax.swing.JTextField jTxt_8;
    private javax.swing.JTextField jTxt_9;
    // End of variables declaration//GEN-END:variables
}
