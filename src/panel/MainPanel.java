package panel;

import MNIST.Mnist;
import image.LoadImage;
import network.NeuralNetTools;
import network.NeuralNetwork;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainPanel implements ActionListener {

    JFrame jFrame = new JFrame();
    DrawPanel jPanelDrawImage;
    JLabel jLabelDrawImage = new JLabel();
    JPanel jPanelTitle = new JPanel();
    JPanel jPanelButton = new JPanel();
    JLabel jLabel = new JLabel();
    JButton trainModelBtn = new JButton();
    JButton openImageBtn = new JButton();
    JButton predictImage = new JButton();
    JButton testModel = new JButton();
    JButton loadModelBtn = new JButton();
    JButton clearBtn = new JButton();
    NeuralNetwork network;

    public MainPanel(){

        jPanelDrawImage = new DrawPanel();

        //main frame
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(1000,600);
        jFrame.getContentPane().setBackground(new Color(50,50,50));
        jFrame.setLayout(new BorderLayout());
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);

        //main heading of main frame
        jLabel.setBackground(new Color(25,25,25));
        jLabel.setForeground(new Color(25,255,0));
        jLabel.setFont(new Font("Ink Free",Font.BOLD,30));
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setText("Identification of handwritten numbers");
        jLabel.setOpaque(true);
        jPanelTitle.setLayout(new BorderLayout());
        jPanelTitle.setBounds(100,100,50,50);
        jPanelTitle.add(jLabel);

        //train btn and open file btn
        jPanelButton.add(trainModelBtn,BorderLayout.LINE_START);
        trainModelBtn.setFont(new Font("MV Boli",Font.BOLD,15));
        trainModelBtn.setText("Train Model");
        jPanelButton.add(openImageBtn,BorderLayout.LINE_END);
        openImageBtn.setFont(new Font("MV Boli",Font.BOLD,15));
        openImageBtn.setText("Open Image");
        jPanelButton.add(predictImage,BorderLayout.LINE_END);
        predictImage.setFont(new Font("MV Boli",Font.BOLD,15));
        predictImage.setText("Predict Image");
        testModel.setFont(new Font("MV Boli",Font.BOLD,15));
        testModel.setText("Test Accuracy");
        jPanelButton.add(testModel,BorderLayout.LINE_END);
        loadModelBtn.setFont(new Font("MV Boli",Font.BOLD,15));
        loadModelBtn.setText("Load Model");
        jPanelButton.add(loadModelBtn,BorderLayout.LINE_END);
        clearBtn.setFont(new Font("MV Boli",Font.BOLD,15));
        clearBtn.setText("Clear Drawing");
        jPanelButton.add(clearBtn,BorderLayout.LINE_END);
//        jPanelButton.setLayout(new GridLayout(1,2));
        jPanelButton.setBackground(new Color(150,150,150));


        jFrame.add(jPanelButton);
        jFrame.add(jPanelTitle,BorderLayout.NORTH);
        jFrame.getContentPane().add(jPanelDrawImage, BorderLayout.WEST);


//        clearBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
////                jPanelDrawImage.removeAll();
////                jPanelDrawImage.revalidate();
////                jPanelDrawImage.repaint();
//
////                Component[] components = jPanelDrawImage.getComponents();
////                for (Component componentTemp: components){
////                    jPanelDrawImage.remove(componentTemp);
////                }
////                jPanelDrawImage.revalidate();
////                jPanelDrawImage.repaint();
//                jFrame.getContentPane().remove(jPanelDrawImage);
//                jPanelDrawImage.removeAll();
//                jPanelDrawImage.revalidate();
//                jPanelDrawImage.repaint();
//                jFrame.getContentPane().add(jPanelDrawImage, BorderLayout.WEST);
//
//            }
//        });

        openImageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();

                int returnVal = fc.showOpenDialog(jFrame);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    //This is where a real application would open the file.
                    System.out.println("Opening: " + file.getName());
                    if(network == null){
                        Mnist mnist = new Mnist();
                        NeuralNetwork neuralNetwork = mnist.createNetwork();
                        network = neuralNetwork;
                        int output = NeuralNetTools.largestValueIndexInArray(neuralNetwork.calculateImage1(fc.getSelectedFile().getAbsolutePath()));
                        showImageInFrame(fc, output);
                        System.out.println("The output is: " + output);
                    }else{
                        int output = NeuralNetTools.largestValueIndexInArray(network.calculateImage1(fc.getSelectedFile().getAbsolutePath()));
                        System.out.println("The output is: " + output);
                        showImageInFrame(fc, output);
                        JFrame f=new JFrame();
                        JOptionPane.showMessageDialog(f,"Your number is : " + output);
                    }
                } else {
                    System.out.println("Open command cancelled by user.");
                }
            }
        });

        loadModelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f=new JFrame();
                JOptionPane.showMessageDialog(f,"Loading neural network..... Click OK to continue");
                Mnist mnist = new Mnist();
                NeuralNetwork neuralNetwork = mnist.createNetwork();
                network = neuralNetwork;
//                JFrame f1=new JFrame();
                f.getContentPane().removeAll();
                JOptionPane.showMessageDialog(f,"Neural network model is now loaded");
            }
        });

        trainModelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f=new JFrame();
                JOptionPane.showMessageDialog(f,"Training neural network..... Click OK to continue");
                f.setVisible(false);
                Mnist mnist = new Mnist();
                NeuralNetwork neuralNetwork = mnist.trainNetwork(new NeuralNetwork(784, 196, 100, 70, 35, 10)); //add only if training is complete
                network = neuralNetwork;
                network.saveModel(neuralNetwork);
                JFrame f1=new JFrame();
                JOptionPane.showMessageDialog(f1,"Neural network model is now trained");
            }
        });

        testModel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(network == null){
                    JFrame f=new JFrame();
                    JOptionPane.showMessageDialog(f,"Neural network is not trained yet, train a new model or load a saved model");
                }else{
                    JFrame f=new JFrame();
                    JOptionPane.showMessageDialog(f,"Testing the accuracy of the neural network..... Click OK to continue");
                    f.setVisible(false);
                    Mnist mnist = new Mnist();
                    double accuracy = mnist.testNetwork(network);
                    JFrame f1=new JFrame();
                    JOptionPane.showMessageDialog(f1,"Neural network accuracy is : " + accuracy + "%");
                }
            }
        });

        predictImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(network == null){
                    Mnist mnist = new Mnist();
                    NeuralNetwork neuralNetwork = mnist.createNetwork();
                    jPanelDrawImage.saveImageN("imggyg","png",LoadImage.createImage(jPanelDrawImage) );

                    int output = NeuralNetTools.largestValueIndexInArray(neuralNetwork.calculate(LoadImage.ImageLoaderFromBufferedImage(LoadImage.createImage(jPanelDrawImage))));
                    System.out.println("The output is: " + output);
                }else{
                    int output = NeuralNetTools.largestValueIndexInArray(network.calculate(LoadImage.ImageLoaderFromBufferedImage(LoadImage.createImage(jPanelDrawImage))));
                    System.out.println("The output is: " + output);
                }
            }
        });
    }

    void showImageInFrame(JFileChooser fc, int output){
        BufferedImage myPicture = null;
        try {
            String path = new File("").getAbsolutePath();
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            System.out.println(fc.getSelectedFile().getAbsolutePath());
//                            myPicture = ImageIO.read(new File(path + "/res/" +file.getName()));path + "/res/" +file.getName()));
            myPicture = ImageIO.read(new File(fc.getSelectedFile().getAbsolutePath()));
//                            jPanelDrawImage.loadImage(myPicture);
//                            LoadImage.ImageLoaderFromBufferedImage(myPicture);

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Graphics2D g = (Graphics2D) myPicture.getGraphics();
        g.setStroke(new BasicStroke(3));
        g.setColor(Color.BLUE);
        g.drawRect(10, 10, myPicture.getWidth() - 20, myPicture.getHeight() - 20);

        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        JPanel jPanel = new JPanel();
        jPanel.add(picLabel);
        JFrame f = new JFrame();
        f.setSize(new Dimension(myPicture.getWidth(), myPicture.getHeight()));
        f.add(jPanel);
        f.setVisible(true);
        f.setLocationRelativeTo(null);

        JFrame f1=new JFrame();
        JOptionPane.showMessageDialog(f1,"Your number is : " + output);
        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new MainPanel();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent){

    }
}

class DrawPanel extends JPanel implements MouseMotionListener, MouseListener {

    private BufferedImage bufferedImage;
    private static final long serialVersionUID = 2789376983756851940L;
    public int[] image_buffer = new int[784];

    Point firstPoint = new Point(0,0);
    Point secondPoint = new Point(0,0);


    public DrawPanel(){
        addMouseMotionListener(this);
        setPreferredSize( new Dimension( 500, 500 ) );
//        setBackground(Color.black);
        setBorder(new EmptyBorder(5, 5, 5, 5));
    }



    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

        Graphics graphics = this.getGraphics();
        graphics.setColor(Color.white);
        graphics.fillOval(e.getX(),e.getY(),10,10);

//        if(secondPoint.x != 0 && secondPoint.y != 0){
//            firstPoint.x = secondPoint.x;
//            firstPoint.y = secondPoint.y;
//        }
//        secondPoint.setLocation(e.getX(),e.getY());
//        paint(getGraphics());
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        if(bufferedImage != null){
            g2d.drawImage(bufferedImage,0,0,bufferedImage.getWidth(),bufferedImage.getHeight(),this);
            SwingUtilities.updateComponentTreeUI(this);
        }

//

//        int w = this.getWidth();
//        int h = this.getHeight()-100;
//        System.out.println(w+ "   " + h);
//        g.clearRect(0, 0, w, h);
//        for(int i = 0; i < 784; i++){
//            int x = (i % 28) * w / 28;
//            int y = ((int)i / (int)28) * h / 28;
//            g2d.setColor(new Color(image_buffer[i]));
//            g2d.fillRect(x, y, w / 28 + 1, h / 28 + 1);
//        }
//        Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f);
//        g2d.setComposite(comp);

//        g.drawLine(firstPoint.x,firstPoint.y,secondPoint.x,secondPoint.y);
    }

    // Make sure that the "bufferedImage" is non-null
    // and has the same size as this panel
    private void validateImage()
    {
        if (bufferedImage == null){
            bufferedImage = new BufferedImage(
                    getWidth(), getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics g = bufferedImage.getGraphics();
            g.setColor(getBackground());
            g.fillRect(0,0,getWidth(),getHeight());
            g.dispose();

        }
        if (bufferedImage.getWidth() != getWidth() ||
                bufferedImage.getHeight() != getHeight())
        {
            BufferedImage newBufferedImage = new BufferedImage(
                    getWidth(), getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics g = newBufferedImage.getGraphics();
            g.setColor(getBackground());
            g.fillRect(0,0,getWidth(),getHeight());
            g.drawImage(bufferedImage, 0,0,null);
            g.dispose();
            bufferedImage = newBufferedImage;
        }
    }

    public void saveImageN(String name,String type,BufferedImage bufferedImage) {
        BufferedImage image = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
//        Graphics2D g2 = image.createGraphics();
        LoadImage.ImageLoaderFromBufferedImage(image);
        LoadImage.ImageLoaderFromBufferedImage(bufferedImage);
        Graphics g2 = image.getGraphics();
        Graphics2D g2d = (Graphics2D) g2;
        g2.setColor(Color.white);
        paintComponent(g2d);
//        paint(g2);
        try{
            ImageIO.write(image, type, new File(name+"."+type));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadImage(BufferedImage bufferedImage){
        this.bufferedImage = bufferedImage;
        Graphics g = bufferedImage.getGraphics();
        paint(g);
//        repaint();
    }

//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//    }

    public void saveImage(String name,String type) {

//        BufferedImage imagebuf=null;
//        try {
//            imagebuf = new Robot().createScreenCapture(jPanel.bounds());
//        } catch (AWTException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }

        bufferedImage = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImage.createGraphics();
//        Graphics g = bufferedImage.getGraphics();
        g2.drawImage(bufferedImage, 0, 0, null);
        paint(g2);
        repaint();
        try{
            ImageIO.write(bufferedImage, type, new File(name+"."+type));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(secondPoint.x == 0 && secondPoint.y == 0){
            secondPoint.x = e.getX();
            secondPoint.y = e.getY();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(firstPoint.x == 0 && firstPoint.y == 0){
            firstPoint.x = e.getX();
            firstPoint.y = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

class GraphicsPanel extends JPanel{

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.setBackground(Color.BLACK);
    }
}
