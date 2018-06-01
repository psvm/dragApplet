/*
 * Authors: unknown
 * Date: forget
 * (c) some company
 * This programme was made just for fun
 */

package drag;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Event;


//========================================
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


//============================================
public class Drag extends Applet {

    private Graphics g;
    private Image background;
    private Image home;
    private Coordinate coordinate;
    private boolean doneLoading = false;
    private boolean selection = false;
    private int backgroundWidth;
    private int backgroundHeight;
    private int newX;
    private int newY;
    private int homeWidth;
    private int homeHeight;

    //=====================================================================
    public void init() {
        try {

            g = getGraphics();
            // Получение изображения для фона и объекта из файлов
            background = getImage(new URL("file:///D:/123/sat.jpg"));
            home = getImage(new URL("file:///D:/123/mir.gif"));

            backgroundWidth = size().width;
            backgroundHeight = size().height;
            homeWidth = home.getWidth(this);
            homeHeight = home.getHeight(this);

            coordinate = new Coordinate(100, 100);

            /* Создается изображение вне экрана ( createImage), а
             * затем выводится на экран функцией drawImage.
             */
            Image offScrImage = createImage(backgroundWidth, backgroundHeight);
            Graphics offScrGC = offScrImage.getGraphics();
            offScrGC.drawImage(background, 0, 0, backgroundWidth, backgroundHeight, this);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Drag.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    //========================================================
    private void moveObject() {

        int oldX = coordinate.x;//Сохранение координат предыдущего (" старого")
        int oldY = coordinate.y;// местоположени


        Graphics g2;
        g2 = g.create();

        /*       Затем clipRect создает  прямоугольник отсечения ( там где раньше
         * было изображение объекта )
         */
        g2.clipRect(oldX, oldY, homeWidth, homeHeight);

        /*       Если "новое" изображение объекта попадает полностью в границы
         *        апплета, то новые координаты становяться текущими. Затем восстанавливается
         *        фоновое изображение и выводится объект уже в новом месте с координатами
         *        newX и newY
         */
        if (((newX > 0) & (newX < (backgroundWidth - homeWidth))) & ((newY > 0) &
                (newY < (backgroundHeight - homeHeight)))) {
            coordinate.x = newX;
            coordinate.y = newY;
            g2.drawImage(background, 0, 0, null);
            g.drawImage(home, newX, newY, this);
            repaint();
        }
    }

    //=====================================================================
    /* Если указатель мыши помещается в заданную область аплета,
     * и нажимается любая клавиша мыши,
     * то  определяется: принадлежит ли данная точка объекту или это фон,
     * Если принадлежит, то selection становится истиной до тех пор,
     * пока клавишу не отпустят.
     *
     */
    public boolean mouseDown(Event evt, int x, int y) {
        homeWidth = home.getWidth(this);
        homeHeight = home.getHeight(this);
        if ((x > coordinate.x) && (x < (coordinate.x + homeWidth)) && (y > coordinate.y) &&
                (y < (coordinate.y + homeHeight))) {
            selection = true;
        }

        return true;
    }

    //======================================================================
    public boolean mouseUp(Event evt, int x, int y) {
        selection = false;
        return true;
    }

    //======================================================================
    // Перемещение мыши с нажатой клавишей
    public boolean mouseDrag(Event evt, int x, int y) {
        if (selection) {
            newX = x;
            newY = y;
            moveObject();
        }
        return true;
    }

    //======================================================================
    public boolean imageUpdate(Image img, int infoflags, int x, int y,
                               int w, int h) {
        /* Пока фоновое изображение загружается, параметр infoflags не равен ALLBITS. Переменная done_loading
         * равна false, и в строке статуса Вы видите Loading
         */
        if (infoflags == ALLBITS) {
            doneLoading = true;
            repaint();

            return false;
        } else {
            return true;
        }
    }

    //======================================================
    public void paint(Graphics g) {
        if (!doneLoading) {
            showStatus("Loading");
        } else {
            showStatus("OK");
            g.drawImage(background, 0, 0, this);
            g.drawImage(home, coordinate.x, coordinate.y, this);
        }
    }
//==============================================
}