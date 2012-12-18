import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.SupervisedTrainingElement;

import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.neuroph.util.TransferFunctionType;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.*;
import java.awt.event.*;
/**
 * Scott Dyer
 * 
 * Artificial Intelligence Final
 * 
 * This program has a small training set of two types of 4s and a few h characters
 * that train a multilayer perceptron in order to detect a drawn 4.
 * ButtonPixel class handles the user input and mouse events to map pixel state
 * to the OCRnet array of doubles that will be the input to the neural net
 * This code base makes use of neuroph java library 2.6 available from
 * http://neuroph.sourceforge.net/download.html
 * The gui consist of an 8x8 pixel map implement by the ButtonPixel, a button "In Set"
 * that adds a user drawn 4 to the set of positive examples in the training set,
 * a button Out Set that adds a user drawn character to the negative training set,
 * and a button test that puts the current drawn character through the neural net
 * and provides the likelihood that it is a 4. The likelihood is a float that should
 * be interpreted as a 4 if the value is above 0.5
 */
		
public class OCRnet extends JPanel implements MouseListener, ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ButtonPixel pixelTable[][];
	int xpixels = 8;
	int ypixels = 8;
	String inTrainingDataCmd = "inTrainingData";
	String outTrainingDataCmd = "outTrainingData";
	String testCmd = "testCmd";
	JTextField statusText; // tells the user the likelyhood of it being a 4
	double[] pixelMap; // the "screen" that the character is drawn too 
    // The training set
    TrainingSet<SupervisedTrainingElement> trainingSet;
    // my multi layer perceptron
    MultiLayerPerceptron myMlPerceptron;
    // Va
    static final double[] four0 = { 
	0,0,0,0,0,0,0,0,
	0,0,1,0,0,1,0,0,
	0,0,1,0,0,1,0,0,
	0,0,1,0,0,1,0,0,
	0,0,1,1,1,1,0,0,
	0,0,0,0,0,1,0,0,
	0,0,0,0,0,1,0,0,
	0,0,0,0,0,0,0,0};
    
    static final double[] four1 = { 
	0,0,0,0,0,0,0,0,
	0,0,1,0,0,1,0,0,
	0,0,1,0,0,1,0,0,
	0,0,1,1,1,1,0,0,
	0,0,0,0,0,1,0,0,
	0,0,0,0,0,1,0,0,
	0,0,0,0,0,1,0,0,
	0,0,0,0,0,0,0,0};
    
    static final double[] four2 = { 
	0,0,0,0,0,0,0,0,
	0,0,1,0,0,1,0,0,
	0,0,1,0,0,1,0,0,
	0,0,1,1,1,1,1,0,
	0,0,0,0,0,1,0,0,
	0,0,0,0,0,1,0,0,
	0,0,0,0,0,1,0,0,
	0,0,0,0,0,0,0,0};
    
    static final double[] four3 = { 
	0,0,0,0,0,0,0,0,
	0,0,0,0,1,1,0,0,
	0,0,0,1,0,1,0,0,
	0,0,1,0,0,1,0,0,
	0,1,1,1,1,1,0,0,
	0,0,0,0,0,1,0,0,
	0,0,0,0,0,1,0,0,
	0,0,0,0,0,0,0,0};
    
    static final double[] four4 = { 
	0,0,0,0,0,0,0,0,
	0,0,0,0,0,1,1,0,
	0,0,0,0,1,0,1,0,
	0,0,0,1,0,0,1,0,
	0,0,1,1,1,1,1,0,
	0,0,0,0,0,0,1,0,
	0,0,0,0,0,0,1,0,
	0,0,0,0,0,0,0,0};
    
    static final double[] four5 = { 
	0,0,0,0,1,0,0,0,
	0,0,0,1,1,0,0,0,
	0,0,1,0,1,0,0,0,
	0,1,0,0,1,0,0,0,
	1,1,1,1,1,1,0,0,
	0,0,0,0,1,0,0,0,
	0,0,0,0,1,0,0,0,
	0,0,0,0,0,0,0,0};
    
    static final double[] four6 = { 
	0,0,0,1,1,0,0,0,
	0,0,1,0,1,0,0,0,
	0,1,0,0,1,0,0,0,
	0,1,0,0,1,0,0,0,
	1,1,1,1,1,0,0,0,
	0,0,0,0,1,0,0,0,
	0,0,0,0,1,0,0,0,
	0,0,0,0,0,0,0,0};
    
    static final double[] four7 = { 
	0,0,0,0,0,1,0,0,
	0,0,0,0,0,1,0,0,
	0,0,0,0,1,1,0,0,
	0,0,0,1,0,1,0,0,
	0,1,1,1,1,1,0,0,
	0,0,0,0,0,1,0,0,
	0,0,0,0,0,1,0,0,
	0,0,0,0,0,0,0,0};
    static final double[] four8 = { 
	0,0,0,0,0,0,0,0,
	0,1,0,0,1,0,0,0,
	0,1,0,0,1,0,0,0,
	0,1,0,0,1,0,0,0,
	0,1,1,1,1,0,0,0,
	0,0,0,0,1,0,0,0,
	0,0,0,0,1,0,0,0,
	0,0,0,0,0,0,0,0};
    
    static final double[] h0 = {
	0,0,0,0,0,0,0,0,
	0,0,1,0,0,0,0,0,
	0,0,1,0,0,0,0,0,
	0,0,1,0,0,0,0,0,
	0,0,1,1,1,1,0,0,
	0,0,1,0,0,1,0,0,
	0,0,1,0,0,1,0,0,
	0,0,0,0,0,0,0,0 };

    static final double[] h1 = {
	0,0,0,0,0,0,0,0,
	0,1,0,0,0,0,0,0,
	0,1,0,0,0,0,0,0,
	0,1,0,0,0,0,0,0,
	0,1,1,1,1,0,0,0,
	0,1,0,0,1,0,0,0,
	0,1,0,0,1,0,0,0,
	0,0,0,0,0,0,0,0 };
    static final double[] h2 = {
	0,0,0,0,0,0,0,0,
	0,0,1,0,0,0,0,0,
	0,0,1,0,0,0,0,0,
	0,0,1,1,1,1,0,0,
	0,0,1,0,0,1,0,0,
	0,0,1,0,0,1,0,0,
	0,0,1,0,0,1,0,0,
	0,0,0,0,0,0,0,0 };
    JFrame guiFrame;
    JPanel pixelMapPanel;
    public OCRnet() {
    	//init();
    }
    
    public void init(){
    	pixelMap = new double[xpixels*ypixels];
        // create the training data neural net with one input per pixel and 1 output
        trainingSet = new TrainingSet<SupervisedTrainingElement>(64, 1);

    	guiFrame = new JFrame();
    	guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	guiFrame.setTitle("OCR Neural Network");
    	guiFrame.setSize(400,200);
    	
    	//Background panel to hold all
    	JPanel mainPanel = new JPanel();
    	GridBagLayout mainGridBag = new GridBagLayout();
        mainPanel.setLayout(mainGridBag);
        
        //The pixelMap sub panel
    	pixelMapPanel = new JPanel();
    	GridBagLayout pixelGridBag = new GridBagLayout();
    	pixelMapPanel.setLayout(pixelGridBag);
 
    	// the constraints
        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.NONE;
        cons.gridheight = 1;
        cons.gridwidth = 1;
    	cons.ipady = 2;
    	cons.ipadx = 2;
 
    	pixelTable = new ButtonPixel[xpixels][ypixels];
    	for (int x = 0 ; x < xpixels ; x++)
    	{
    	    for (int y = 0 ; y < ypixels ; y++)
    	    {
    	    	pixelTable[x][y] = new ButtonPixel("",x, y, this.xpixels, this.ypixels, pixelMap);
    	    	pixelTable[x][y].addMouseListener(this);
    	    	cons.gridx = x;
    	    	cons.gridy = y;
    	    	pixelTable[x][y].setVisible(true);
    	    	pixelGridBag.setConstraints(pixelTable[x][y], cons);
    	    	pixelMapPanel.add(pixelTable[x][y],cons);
    	    }
    	}
    	mainPanel.setLayout(mainGridBag);
    	cons.gridx = 0;
    	cons.gridy = 0;
        cons.gridheight = 8;
        cons.gridwidth = 8;
    	cons.anchor = GridBagConstraints.NORTH;
    	mainGridBag.setConstraints(pixelMapPanel, cons);
    	mainPanel.add(pixelMapPanel,cons);
    	//button to add as IN training set
    	JButton inButton = new JButton("In Set");
    	inButton.setActionCommand(inTrainingDataCmd);
    	inButton.addActionListener( this);
    	cons.gridx = 0;
    	cons.gridy = 9;
        cons.gridheight = 2;
        cons.gridwidth = 2;
    	cons.fill = GridBagConstraints.NONE;
    	cons.anchor = GridBagConstraints.WEST;
    	mainGridBag.setConstraints(inButton, cons);
    	mainPanel.add(inButton,cons);
    	//button to add as OUT training set
    	JButton outButton = new JButton("OUT Set");
    	outButton.setActionCommand(outTrainingDataCmd);
    	outButton.addActionListener( this);
    	cons.gridx = 3;
    	cons.gridy = 9;
        cons.gridheight = 2;
        cons.gridwidth = 2;
    	cons.fill = GridBagConstraints.NONE;
    	cons.anchor = GridBagConstraints.CENTER;
    	mainGridBag.setConstraints(outButton, cons);
    	mainPanel.add(outButton,cons);
    	//button to test current character in the pixel map
    	JButton testButton = new JButton("TEST");
    	testButton.setActionCommand(testCmd);
    	testButton.addActionListener( this);
    	cons.gridx = 5;
    	cons.gridy = 9;
        cons.gridheight = 2;
        cons.gridwidth = 8;
    	cons.fill = GridBagConstraints.NONE;
    	cons.anchor = GridBagConstraints.EAST;
    	mainGridBag.setConstraints(testButton, cons);
    	mainPanel.add(testButton,cons);
    	// Textfield to let user know what is going on...
    	statusText = new JTextField("started");
     	cons.gridx = 0;
    	cons.gridy = 11;
        cons.gridheight = 2;
        cons.gridwidth = 8;
    	cons.fill = GridBagConstraints.BOTH;
    	cons.anchor = GridBagConstraints.NORTH;
    	mainGridBag.setConstraints(statusText, cons);
    	mainPanel.add(statusText,cons);
    	
    	
    	
    	guiFrame.add(mainPanel);
    	
    	
    	guiFrame.setBackground(Color.red);
    	guiFrame.setVisible(true);
    	//Add some canned values trained on 4 and h in and out respectively
		trainingSet.addElement(new SupervisedTrainingElement(four0, new double[]{1}));
		trainingSet.addElement(new SupervisedTrainingElement(four1, new double[]{1}));
		trainingSet.addElement(new SupervisedTrainingElement(four2, new double[]{1}));
		trainingSet.addElement(new SupervisedTrainingElement(four3, new double[]{1}));
		trainingSet.addElement(new SupervisedTrainingElement(four4, new double[]{1}));
		trainingSet.addElement(new SupervisedTrainingElement(four5, new double[]{1}));
		trainingSet.addElement(new SupervisedTrainingElement(four5, new double[]{1}));
		trainingSet.addElement(new SupervisedTrainingElement(four6, new double[]{1}));
		trainingSet.addElement(new SupervisedTrainingElement(four7, new double[]{1}));
		trainingSet.addElement(new SupervisedTrainingElement(four8, new double[]{1}));
		trainingSet.addElement(new SupervisedTrainingElement(h0, new double[]{0}));
		trainingSet.addElement(new SupervisedTrainingElement(h1, new double[]{0}));
		trainingSet.addElement(new SupervisedTrainingElement(h2, new double[]{0}));
    }

    public void actionPerformed(ActionEvent e){
    	if( inTrainingDataCmd == e.getActionCommand()){
    		double[] inchar  = pixelMap.clone();
            trainingSet.addElement(new SupervisedTrainingElement(inchar, new double[]{1}));
    		this.statusText.setText("Added to IN data");
    	}
    	if( this.outTrainingDataCmd == e.getActionCommand()){
    		double[] outchar  = pixelMap.clone();
            trainingSet.addElement(new SupervisedTrainingElement(outchar, new double[]{0}));
    		this.statusText.setText("Added to OUT Data");
    	}
    	if( this.testCmd == e.getActionCommand()){
    		this.statusText.setText("Testing");
    		double[] testChar = pixelMap.clone();
            // create multi layer perceptron
            myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, 64, 8, 1);

            // learn the training set
    		System.out.println("Learning...");
            myMlPerceptron.learn(trainingSet);
    		System.out.println("setting input...");
            myMlPerceptron.setInput(testChar);
    		System.out.println("calculating...");
            myMlPerceptron.calculate();
            double[ ] networkOutput = myMlPerceptron.getOutput();
            System.out.println(" Output: " + Arrays.toString(networkOutput) );
            this.statusText.setText(Arrays.toString(networkOutput));
    	}
	}

	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
			//UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		 } catch (Exception e) {
		            e.printStackTrace();
		 }
		OCRnet ocrNet = new OCRnet();
		ocrNet.init();
		if (false){
			// create training set
			TrainingSet<SupervisedTrainingElement> trainingSet = new TrainingSet<SupervisedTrainingElement>(64, 1);
			trainingSet.addElement(new SupervisedTrainingElement(four0, new double[]{1}));
			trainingSet.addElement(new SupervisedTrainingElement(four1, new double[]{1}));
			trainingSet.addElement(new SupervisedTrainingElement(four2, new double[]{1}));
			trainingSet.addElement(new SupervisedTrainingElement(four3, new double[]{1}));
			trainingSet.addElement(new SupervisedTrainingElement(four4, new double[]{1}));
			trainingSet.addElement(new SupervisedTrainingElement(four5, new double[]{1}));
			trainingSet.addElement(new SupervisedTrainingElement(h0, new double[]{0}));
			trainingSet.addElement(new SupervisedTrainingElement(h1, new double[]{0}));
			trainingSet.addElement(new SupervisedTrainingElement(h2, new double[]{0}));
			TrainingSet<SupervisedTrainingElement> testSet = new TrainingSet<SupervisedTrainingElement>(64,1);
			testSet.addElement(new SupervisedTrainingElement(four5, new double[]{1}));
			testSet.addElement(new SupervisedTrainingElement(four6, new double[]{1}));
			testSet.addElement(new SupervisedTrainingElement(four7, new double[]{1}));
			testSet.addElement(new SupervisedTrainingElement(h0, new double[]{0}));
			testSet.addElement(new SupervisedTrainingElement(h1, new double[]{0}));
			testSet.addElement(new SupervisedTrainingElement(h2, new double[]{0}));

			// create multi layer perceptron
			MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, 64, 8, 1);
			// learn the training set
			myMlPerceptron.learn(trainingSet);

			// test perceptron
			System.out.println("Testing trained neural network");
			testNeuralNetwork(myMlPerceptron, trainingSet);

			// save trained neural network
			myMlPerceptron.save("myOCR.nnet");

			// load saved neural network
			NeuralNetwork loadedMlPerceptron = NeuralNetwork.load("myOCR.nnet");

			// test loaded neural network
			System.out.println("Testing loaded neural network");
			testNeuralNetwork(loadedMlPerceptron, testSet);
		}
    }

    public static void testNeuralNetwork(NeuralNetwork nnet, TrainingSet tset) {
    	List<TrainingElement> training_elements = tset.elements();
    	
        for(TrainingElement trainingElement : training_elements) {

            nnet.setInput(trainingElement.getInput());
            nnet.calculate();
            double[ ] networkOutput = nnet.getOutput();
            System.out.print("Input: " + Arrays.toString(trainingElement.getInput()) );
            System.out.println(" Output: " + Arrays.toString(networkOutput) );

        }
    }
    /////////////////////////////////////////////////////////////////
    //These are not used but are necessary for mouseListener
    public void mouseEntered (MouseEvent e)
    {
    	//this.guiFrame.repaint();
    }

    public void mouseClicked(MouseEvent e){
    	//this.guiFrame.repaint();
    }

    public void mouseExited (MouseEvent e)
    {
    	//System.out.println("OCRnet");
    }
    

    public void mousePressed (MouseEvent e)
    {
    	//System.out.println("OCRnet");
    }
    

    public void mouseReleased (MouseEvent e)
    {
    	//System.out.println("OCRnet");
    }
}