import java.awt.*;
import java.util.*;
import java.lang.Math;
import java.awt.event.*;//mentioned specifically close button k liye

public class FishTank extends Frame implements Runnable {
    //Class Variables
    MediaTracker tracker;//a media tracker ref variable
    Image bgimage;
    Image[] fishImages = new Image[2];
    Thread thread;

    //storing the number of fishes and each individual fish object in VEctor class
    int numberFish = 61;
    Vector<Fish> fishes = new Vector<Fish>();
    boolean runOK = true;//thread ko close krne k liye
    Image memory;
    Graphics memoryGraphics;

    //Fish Tank Constructor , Some part of Major Work isme krenge
    FishTank() {
        setTitle("THE FISH TANK");//title of frame
        //we put gif images in arrays andimage obj buttrackeris the one which loadsthem on screen when run
        //Just like Scanner we define a media tracker object its used in loading the images
        tracker = new MediaTracker(this);


        //Adding the Fish into Fish Array which will be latter use to load these images from here

        fishImages[0] = Toolkit.getDefaultToolkit().getImage("fish1.gif");//Thereis a class known as Toolkit which cointain many tools , we can use its static method getDefaultToolkit method
        tracker.addImage(fishImages[0], 0);//Tracker is use to load image when needed ,it has one id int which is prefence in which the images is loaded

        fishImages[1] = Toolkit.getDefaultToolkit().getImage("fish2.gif");
        tracker.addImage(fishImages[1], 0);

        //Adding th bg image to Image variable;
        bgimage = Toolkit.getDefaultToolkit().getImage("bubbles.gif");
        tracker.addImage(bgimage, 0);

        try {
            tracker.waitForID(0);
        } catch (Exception e) {
            System.out.println("0");
        }


        setSize(bgimage.getWidth(this), bgimage.getHeight(this));
        setResizable(false);
        setVisible(true);

        memory = createImage(getSize().width, getSize().height);//concept of memory image
        memoryGraphics = memory.getGraphics();


        //Creating a new Thread
        thread = new Thread(this);
        thread.start();

        //This is the Working of close Button
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(
                    WindowEvent windowEvent) {
                System.exit(0);
            }
                               }
        );
    }


    //Main Class , only work is to call the Tank Constructor
    public static void main(String[] args) {
        new FishTank();
    }




    //run method of thread which actual do the drawing

    public void run() {

        //Getting the size of tank in a rectangle object , so fish knew the boundaries of swimming
        Rectangle edges = new Rectangle(
                0+getInsets().left, 0+getInsets().top,
                getSize().width - (getInsets().left + getInsets().right),
                getSize().height - (getInsets().top + getInsets().bottom)
        );

        for (int i = 0; i < numberFish; i++) {
            fishes.add(new Fish(fishImages[0], fishImages[1], edges, this));
            try {
                Thread.sleep(30);
            } catch (Exception exp) {
                System.out.println(exp.getMessage());
            }
        }
        Fish fish;//created a fish refrence variable
        while (runOK) {
            for (int i = 0; i < numberFish; i++) {
                fish = fishes.elementAt(i);
                fish.swim();
            }
            try {
                Thread.sleep(110);
            } catch (Exception e) {
                runOK = false;
                System.out.println("0");
            }
            repaint();
        }

    }
    //theat repaint maethod call its update method

    public void update(Graphics g) {
        memoryGraphics.drawImage(bgimage, 0, 0, this);

        for (int i = 0; i < numberFish; i++) {
            //???????
            fishes.elementAt(i).drawFishImage(memoryGraphics);
        }
        g.drawImage(memory, 0, 0, this);

    }

}

    class Fish {
        Component tank;
        Image image1;
        Image image2;
        Point location;
        Point velocity;
        Rectangle edges;
        Random random;

        public Fish(Image image1, Image image2, Rectangle edges, Component tank) {
            random = new Random(System.currentTimeMillis());
            this.tank = tank;
            this.image1 = image1;
            this.image2 = image2;
            this.edges = edges;

            this.location = new Point(100 + (Math.abs(random.nextInt()) % 400),
                    100 + (Math.abs(100 + random.nextInt()) % 100));
            this.velocity = new Point(random.nextInt() % 8,
                    random.nextInt() % 8);
        }

        void swim() {
            if (random.nextInt() % 7 <= 1) {
                //Adjusting the x coordinate
                velocity.x = velocity.x + random.nextInt() % 4;
                velocity.x = Math.min(velocity.x, 10);
                velocity.x = Math.max(velocity.x, -10);

                //adjusting the y coordinate
                velocity.y += random.nextInt() % 3;
                velocity.y = Math.min(velocity.y, 10);
                velocity.y = Math.max(velocity.y, -10);

                location.x += velocity.x;
                location.y += velocity.y;

                if (location.x < edges.x) {
                    location.x = edges.x;
                    velocity.x = -velocity.x;
                }

                if ((location.x + image1.getWidth(tank))
                        > (edges.x + edges.width)) {
                    location.x = edges.x + edges.width -
                            image1.getWidth(tank);
                    velocity.x = -velocity.x;
                }

                if (location.y < edges.y) {
                    location.y = edges.y;
                    velocity.y = -velocity.y;
                }

                if ((location.y + image1.getHeight(tank))
                        > (edges.y + edges.height)) {
                    location.y = edges.y + edges.height -
                            image1.getHeight(tank);
                    velocity.y = -velocity.y;
                }
            }

        }

        void drawFishImage(Graphics g)
        {
            if (velocity.x < 0)
            {
                g.drawImage(image1, location.x,
                        location.y, tank);
            } else
            {
                g.drawImage(image2, location.x,
                        location.y, tank);
            }
        }
    }
