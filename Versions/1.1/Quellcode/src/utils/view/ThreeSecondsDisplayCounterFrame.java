package utils.view;

import com.sun.awt.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import utils.structures.NotifyValue;
import utils.structures.ReferentedValue;
import view.assets.ImageAsset;

public class ThreeSecondsDisplayCounterFrame extends JFrame {

    public static void showCounter(boolean block) {
        NotifyValue<Boolean> timedOut = new NotifyValue<>(Boolean.FALSE);
        timedOut.addListener(timedOut);
        Runnable gui = () -> {
            new ThreeSecondsDisplayCounterFrame(timedOut).setVisible(true);
        };
        SwingUtilities.invokeLater(gui);
        if (block) {
            synchronized (timedOut) {
                while (!timedOut.get()) {
                    try {
                        timedOut.wait();
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }
    }

    private ThreeSecondsDisplayCounterFrame(NotifyValue<Boolean> timedOut) throws HeadlessException {
        super("Display Counter");

        setUndecorated(true);
        AWTUtilities.setWindowOpaque(this, false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        final ReferentedValue<ImageAsset> bigNumber = new ReferentedValue<ImageAsset>();
        setAlwaysOnTop(true);

        try {
            bigNumber.set(ImageAsset.getImageAssetForName("ui/Big-3.png"));
        } catch (IOException ex) {
            System.err.println("bigNumber Grafik nicht gefunden.");
        }

        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {

                ImageAsset asset = bigNumber.get();

                if (asset != null) {
                    asset.drawCentered(g, getWidth() / 2, getHeight() / 2);

                } else {
                    System.err.println("bigNumber ist nicht gesetzt.");
                }
            }
        };
        contentPane.setLayout(null);

        setSize(300, 300);

        setContentPane(contentPane);
        {
            Timer timer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    bigNumber.set(getBigNumberImageAsset(2));
                    repaint();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
        {
            Timer timer = new Timer(2000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    bigNumber.set(getBigNumberImageAsset(1));
                    repaint();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
        {
            Timer timer = new Timer(3000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    timedOut.set(Boolean.TRUE);
                    dispose();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    public ImageAsset getBigNumberImageAsset(int i) {
        try {
            switch (i) {
                case 3:
                    return ImageAsset.getImageAssetForName("ui/Big-3.png");
                case 2:
                    return ImageAsset.getImageAssetForName("ui/Big-2.png");
                case 1:
                    return ImageAsset.getImageAssetForName("ui/Big-1.png");
                default:
                    throw new IllegalArgumentException("1 <= i <= 3");
            }
        } catch (IOException ex) {
            System.err.println("BigNumberImageAsset konnte nicht geladen werden. return null.");
            return null;
        }
    }
}
