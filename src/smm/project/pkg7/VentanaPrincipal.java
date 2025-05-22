/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package smm.project.pkg7;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBuffer;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import sm.image.KernelProducer;
import sm.image.LookupTableProducer;
import sm.image.SepiaOp;
import sm.image.TintOp;
import sm.image.color.GreyColorSpace;
import sm.iu.DialogoFuncionABC;
import sm.rlm.enums.HerramientaDibujo;
import sm.rlm.eventos.LienzoAdapter;
import sm.rlm.eventos.LienzoEvent;
import sm.rlm.graficos.MiElipse;
import sm.rlm.graficos.MiRectangulo;
import sm.rlm.graficos.MiShape;
import sm.rlm.graficos.MiShapeRellenable;
import sm.rlm.image.MiKernelProducer;
import sm.rlm.image.MiLookupTableProducer;
import sm.rlm.image.ModificarTonoOp;
import sm.rlm.image.PopArtOp;
import sm.rlm.image.PosterizarOp;
import sm.rlm.image.RojoOp;
import sm.rlm.iu.Lienzo2D;

/**
 *
 * @author rober 
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    ManejadorVentanaInterna mvi;
    ManejadorLienzo ml;
    private DialogoFuncionABC funcionABC = null;
    private BufferedImage imgFuente = null;
    
    /**
     * Creates new form VentanaPrincipal
     */
    public VentanaPrincipal() {
        initComponents();
        mvi = new ManejadorVentanaInterna();
        ml = new ManejadorLienzo();
        this.jLabel1.setText("Barra de Estado");
        this.jLabel2.setText("Coordenadas: (0, 0)   RGB Pixel: (0, 0, 0)");
        this.jSlider6.setEnabled(false);
        this.jSlider7.setEnabled(false);
        this.jSlider8.setEnabled(false);
        this.jSlider10.setEnabled(false);
        this.jSlider11.setEnabled(false);
    }
    
    private Lienzo2D getSelectedLienzo() {
        VentanaInterna vi;
        vi = (VentanaInterna) jDesktopPane1.getSelectedFrame();
        return vi != null ? vi.getLienzo2D() : null;
    }
    
    protected void actualizarCoordenadas(int x, int y) {
        this.jLabel2.setText("Coordenadas: (" + x + ", " + y + ")");
    }
    
    protected void actualizarRgb(int r, int g, int b) {
        String text = this.jLabel2.getText() + "   RGB Pixel: (" + r + "," + g + "," + b + ")";
        this.jLabel2.setText(text);
    }
    
    private void turnOffBooleans(Lienzo2D lienzo) {
        lienzo.setMover(false);
        lienzo.setFijar(false);
        lienzo.setBorrar(false);
        lienzo.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }
    
    private String getExtension(File file) {
        String name = file.getName();
        int i = name.lastIndexOf('.');
        if (i > 0 && i < name.length() - 1) {
            return name.substring(i + 1).toLowerCase();
        }
        return null;
    }
    
    private void initLienzoByDefault() {
        jToggleButton1.setSelected(true);
        jToggleButton2.setSelected(false);
        jToggleButton3.setSelected(false);
        jToggleButton4.setSelected(false);
        jToggleButton5.setSelected(false);
        jToggleButton6.setSelected(false);
        jToggleButton7.setSelected(false);
        jToggleButton8.setSelected(false);
        jToggleButton9.setSelected(false);
        jToggleButton10.setSelected(false);
        jButton1.setBackground(Color.BLACK);
        jSlider1.setValue(5);
        jLabel1.setText(HerramientaDibujo.LINE.toString());
        jLabel2.setText("Coordenadas: (0, 0)   RGB Pixel: (0, 0, 0)");
    }
    
    private class ManejadorVentanaInterna extends InternalFrameAdapter{
        
        @Override
        public void internalFrameActivated(InternalFrameEvent evt) {
            VentanaInterna vi = (VentanaInterna) evt.getInternalFrame();
            vi.getLienzo2D().addLienzoListener(ml);
            jToggleButton1.setSelected(vi.getLienzo2D().getHerramienta().equals(HerramientaDibujo.LINE));
            jToggleButton2.setSelected(vi.getLienzo2D().getHerramienta().equals(HerramientaDibujo.RECTANGLE));
            jToggleButton3.setSelected(vi.getLienzo2D().getHerramienta().equals(HerramientaDibujo.ELLIPSE));
            jToggleButton4.setSelected(vi.getLienzo2D().getHerramienta().equals(HerramientaDibujo.QUADCURVE));
            jToggleButton5.setSelected(vi.getLienzo2D().getMover());
            jToggleButton6.setSelected(vi.getLienzo2D().getRelleno());
            jToggleButton7.setSelected(vi.getLienzo2D().getTransparente());
            jToggleButton8.setSelected(vi.getLienzo2D().getAlisado());
            jToggleButton9.setSelected(vi.getLienzo2D().getFijar());
            jToggleButton10.setSelected(vi.getLienzo2D().getBorrar());
            jButton1.setBackground(vi.getLienzo2D().getColor());
            jSlider1.setValue(vi.getLienzo2D().getGrosor());
            
            if (jToggleButton1.isSelected()) {
                jLabel1.setText(HerramientaDibujo.LINE.toString());
            } else if (jToggleButton2.isSelected()) {
                jLabel1.setText(HerramientaDibujo.RECTANGLE.toString());
            } else if (jToggleButton3.isSelected()) {
                jLabel1.setText(HerramientaDibujo.ELLIPSE.toString());
            } else if (jToggleButton4.isSelected()) {
                jLabel1.setText(HerramientaDibujo.QUADCURVE.toString());
            } else if (jToggleButton5.isSelected()) {
                jLabel1.setText("MOVING");
            } else if (jToggleButton9.isSelected()) {
                jLabel1.setText("RECORDING");
            } else if (jToggleButton10.isSelected()) {
                jLabel1.setText("DELETING");
            }

        }
    }
    
    private class ManejadorLienzo extends LienzoAdapter {
        
        @Override
        public void shapeSelected(LienzoEvent evt) {
            MiShape forma = evt.getForma();
            
            if (forma instanceof MiElipse || forma instanceof MiRectangulo) {
                jToggleButton6.setSelected(((MiShapeRellenable) forma).getRellena());
            } else {
                jToggleButton6.setSelected(false);
            }
            jToggleButton7.setSelected(forma.getTransparente());
            jToggleButton8.setSelected(forma.getAlisada());
            jButton1.setBackground(forma.getColor());
            jSlider1.setValue(forma.getGrosor());
        }
        
        @Override
        public void editingModeExited(LienzoEvent evt) {
            Lienzo2D lienzo = (Lienzo2D) evt.getSource();
            
            jToggleButton6.setSelected(lienzo.getRelleno());
            jToggleButton7.setSelected(lienzo.getTransparente());
            jToggleButton8.setSelected(lienzo.getAlisado());
            jButton1.setBackground(lienzo.getColor());
            jSlider1.setValue(lienzo.getGrosor());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jSeparator5 = new javax.swing.JSeparator();
        jToolBar1 = new javax.swing.JToolBar();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jToggleButton5 = new javax.swing.JToggleButton();
        jToggleButton9 = new javax.swing.JToggleButton();
        jToggleButton10 = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jToggleButton6 = new javax.swing.JToggleButton();
        jToggleButton7 = new javax.swing.JToggleButton();
        jToggleButton8 = new javax.swing.JToggleButton();
        jSlider1 = new javax.swing.JSlider();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel3 = new javax.swing.JLabel();
        jSlider2 = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        jSlider3 = new javax.swing.JSlider();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jSlider4 = new javax.swing.JSlider();
        jLabel6 = new javax.swing.JLabel();
        jSlider5 = new javax.swing.JSlider();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jToggleButton11 = new javax.swing.JToggleButton();
        jSlider6 = new javax.swing.JSlider();
        jSlider7 = new javax.swing.JSlider();
        jSlider8 = new javax.swing.JSlider();
        jButton11 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox<>();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jSlider12 = new javax.swing.JSlider();
        jButton12 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jSlider13 = new javax.swing.JSlider();
        jLabel7 = new javax.swing.JLabel();
        jSlider9 = new javax.swing.JSlider();
        jToggleButton12 = new javax.swing.JToggleButton();
        jSlider10 = new javax.swing.JSlider();
        jSlider11 = new javax.swing.JSlider();
        jLabel10 = new javax.swing.JLabel();
        jSlider14 = new javax.swing.JSlider();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar1.setRollover(true);
        jToolBar1.setToolTipText("Barra de Herramientas");

        buttonGroup1.add(jToggleButton1);
        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/linea.png"))); // NOI18N
        jToggleButton1.setToolTipText("Linea");
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton1);

        buttonGroup1.add(jToggleButton2);
        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rectangulo.png"))); // NOI18N
        jToggleButton2.setToolTipText("Rectangulo");
        jToggleButton2.setFocusable(false);
        jToggleButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton2);

        buttonGroup1.add(jToggleButton3);
        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/elipse.png"))); // NOI18N
        jToggleButton3.setToolTipText("Elipse");
        jToggleButton3.setFocusable(false);
        jToggleButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton3);

        buttonGroup1.add(jToggleButton4);
        jToggleButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/curva.png"))); // NOI18N
        jToggleButton4.setToolTipText("Curva");
        jToggleButton4.setFocusable(false);
        jToggleButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton4);

        buttonGroup1.add(jToggleButton5);
        jToggleButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/seleccion.png"))); // NOI18N
        jToggleButton5.setToolTipText("Mover");
        jToggleButton5.setFocusable(false);
        jToggleButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton5);

        buttonGroup1.add(jToggleButton9);
        jToggleButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/fijar.png"))); // NOI18N
        jToggleButton9.setFocusable(false);
        jToggleButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton9);

        buttonGroup1.add(jToggleButton10);
        jToggleButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/eliminar.png"))); // NOI18N
        jToggleButton10.setFocusable(false);
        jToggleButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton10ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton10);
        jToolBar1.add(jSeparator1);

        jPanel1.setMaximumSize(new java.awt.Dimension(30, 30));
        jPanel1.setMinimumSize(new java.awt.Dimension(30, 30));
        jPanel1.setPreferredSize(new java.awt.Dimension(30, 30));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setToolTipText("Color");
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButton1.setMinimumSize(new java.awt.Dimension(30, 30));
        jButton1.setPreferredSize(new java.awt.Dimension(30, 30));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, java.awt.BorderLayout.CENTER);

        jToolBar1.add(jPanel1);

        jToggleButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rellenar.png"))); // NOI18N
        jToggleButton6.setToolTipText("Rellenar");
        jToggleButton6.setFocusable(false);
        jToggleButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton6);

        jToggleButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/transparencia.png"))); // NOI18N
        jToggleButton7.setToolTipText("Transparencia");
        jToggleButton7.setFocusable(false);
        jToggleButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton7);

        jToggleButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/alisar.png"))); // NOI18N
        jToggleButton8.setToolTipText("Alisar");
        jToggleButton8.setFocusable(false);
        jToggleButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton8ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton8);

        jSlider1.setMaximum(50);
        jSlider1.setToolTipText("Grosor");
        jSlider1.setValue(5);
        jSlider1.setPreferredSize(new java.awt.Dimension(50, 20));
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });
        jToolBar1.add(jSlider1);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setToolTipText("Barra de Estado");
        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel1.setText("jLabel1");
        jPanel2.add(jLabel1, java.awt.BorderLayout.WEST);

        jLabel2.setText("jLabel2");
        jPanel2.add(jLabel2, java.awt.BorderLayout.EAST);

        jToolBar2.setRollover(true);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/brillo.png"))); // NOI18N
        jToolBar2.add(jLabel3);

        jSlider2.setMaximum(255);
        jSlider2.setMinimum(-255);
        jSlider2.setValue(0);
        jSlider2.setPreferredSize(new java.awt.Dimension(100, 20));
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });
        jSlider2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jSlider2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jSlider2FocusLost(evt);
            }
        });
        jToolBar2.add(jSlider2);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/contraste.png"))); // NOI18N
        jToolBar2.add(jLabel4);

        jSlider3.setMaximum(20);
        jSlider3.setMinimum(1);
        jSlider3.setValue(10);
        jSlider3.setPreferredSize(new java.awt.Dimension(100, 20));
        jSlider3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider3StateChanged(evt);
            }
        });
        jSlider3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jSlider3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jSlider3FocusLost(evt);
            }
        });
        jToolBar2.add(jSlider3);
        jToolBar2.add(jSeparator2);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Media", "Binomial", "Enfoque", "Relieve", "Fronteras", "Media 5x5", "Media 7x7", "Sobel Horizontal", "Sobel Vertical" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jToolBar2.add(jComboBox1);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/emborronar.png"))); // NOI18N
        jToolBar2.add(jLabel5);

        jSlider4.setMaximum(31);
        jSlider4.setMinimum(1);
        jSlider4.setValue(1);
        jSlider4.setPreferredSize(new java.awt.Dimension(100, 20));
        jSlider4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider4StateChanged(evt);
            }
        });
        jSlider4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jSlider4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jSlider4FocusLost(evt);
            }
        });
        jToolBar2.add(jSlider4);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/perfilar.png"))); // NOI18N
        jToolBar2.add(jLabel6);

        jSlider5.setMaximum(15);
        jSlider5.setMinimum(1);
        jSlider5.setValue(1);
        jSlider5.setPreferredSize(new java.awt.Dimension(100, 20));
        jSlider5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider5StateChanged(evt);
            }
        });
        jSlider5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jSlider5FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jSlider5FocusLost(evt);
            }
        });
        jToolBar2.add(jSlider5);
        jToolBar2.add(jSeparator6);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/contraste2.png"))); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton4);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/ocurecer.png"))); // NOI18N
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton5);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/iluminar.png"))); // NOI18N
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton6);

        jToggleButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/operador2.png"))); // NOI18N
        jToggleButton11.setFocusable(false);
        jToggleButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton11ActionPerformed(evt);
            }
        });
        jToolBar2.add(jToggleButton11);

        jSlider6.setMaximum(255);
        jSlider6.setValue(0);
        jSlider6.setPreferredSize(new java.awt.Dimension(100, 20));
        jSlider6.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider6StateChanged(evt);
            }
        });
        jToolBar2.add(jSlider6);

        jSlider7.setMaximum(255);
        jSlider7.setValue(128);
        jSlider7.setPreferredSize(new java.awt.Dimension(100, 20));
        jSlider7.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider7StateChanged(evt);
            }
        });
        jToolBar2.add(jSlider7);

        jSlider8.setMaximum(255);
        jSlider8.setValue(255);
        jSlider8.setPreferredSize(new java.awt.Dimension(100, 20));
        jSlider8.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider8StateChanged(evt);
            }
        });
        jToolBar2.add(jSlider8);

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/operador1.png"))); // NOI18N
        jButton11.setFocusable(false);
        jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton11);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Negativo", "Sepia Lookup", "Invertir Oscuros", "Invertir Claros" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });
        jToolBar2.add(jComboBox3);
        jToolBar2.add(jSeparator7);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rotar180.png"))); // NOI18N
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton7);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/mas.png"))); // NOI18N
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton8);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/menos.png"))); // NOI18N
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton9);
        jToolBar2.add(jSeparator3);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/bandas.png"))); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton3);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "RGB", "YCC", "GREY" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        jToolBar2.add(jComboBox2);
        jToolBar2.add(jSeparator4);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/combinar.png"))); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton2);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/tintar.png"))); // NOI18N
        jToolBar2.add(jLabel8);

        jSlider12.setValue(0);
        jSlider12.setPreferredSize(new java.awt.Dimension(50, 20));
        jSlider12.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider12StateChanged(evt);
            }
        });
        jSlider12.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jSlider12FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jSlider12FocusLost(evt);
            }
        });
        jToolBar2.add(jSlider12);

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/sepia.png"))); // NOI18N
        jButton12.setFocusable(false);
        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton12);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rojo.png"))); // NOI18N
        jToolBar2.add(jLabel9);

        jSlider13.setMaximum(255);
        jSlider13.setValue(0);
        jSlider13.setPreferredSize(new java.awt.Dimension(50, 20));
        jSlider13.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider13StateChanged(evt);
            }
        });
        jSlider13.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jSlider13FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jSlider13FocusLost(evt);
            }
        });
        jToolBar2.add(jSlider13);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/posterizar.png"))); // NOI18N
        jToolBar2.add(jLabel7);

        jSlider9.setMaximum(20);
        jSlider9.setMinimum(2);
        jSlider9.setValue(2);
        jSlider9.setPreferredSize(new java.awt.Dimension(50, 20));
        jSlider9.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider9StateChanged(evt);
            }
        });
        jSlider9.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jSlider9FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jSlider9FocusLost(evt);
            }
        });
        jToolBar2.add(jSlider9);

        jToggleButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/paleta.png"))); // NOI18N
        jToggleButton12.setFocusable(false);
        jToggleButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton12ActionPerformed(evt);
            }
        });
        jToolBar2.add(jToggleButton12);

        jSlider10.setMaximum(180);
        jSlider10.setValue(0);
        jSlider10.setPreferredSize(new java.awt.Dimension(50, 20));
        jSlider10.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider10StateChanged(evt);
            }
        });
        jToolBar2.add(jSlider10);

        jSlider11.setMaximum(360);
        jSlider11.setValue(0);
        jSlider11.setPreferredSize(new java.awt.Dimension(50, 20));
        jSlider11.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider11StateChanged(evt);
            }
        });
        jToolBar2.add(jSlider11);

        jLabel10.setText("PopArt");
        jToolBar2.add(jLabel10);

        jSlider14.setMaximum(255);
        jSlider14.setValue(128);
        jSlider14.setPreferredSize(new java.awt.Dimension(50, 20));
        jSlider14.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider14StateChanged(evt);
            }
        });
        jSlider14.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jSlider14FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jSlider14FocusLost(evt);
            }
        });
        jToolBar2.add(jSlider14);

        jPanel2.add(jToolBar2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1400, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 182, Short.MAX_VALUE)
        );

        getContentPane().add(jDesktopPane1, java.awt.BorderLayout.CENTER);

        jMenuBar1.setToolTipText("Barra de Menu");

        jMenu1.setText("Archivo");

        jMenuItem1.setText("Nuevo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Abrir");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Guardar");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem10.setText("Duplicar");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem10);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Imagen");

        jMenuItem4.setText("RescaleOp");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem5.setText("ConvolveOp");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem6.setText("BandCombineOp");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem7.setText("ColorConvertOp");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem8.setText("AffineTransformOp");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuItem9.setText("LookupOp");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        Lienzo2D lienzo = this.getSelectedLienzo();
        if (lienzo != null) {
            lienzo.setHerramienta(HerramientaDibujo.LINE);
            this.jLabel1.setText(HerramientaDibujo.LINE.toString());
            this.turnOffBooleans(lienzo);
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        Lienzo2D lienzo = this.getSelectedLienzo();
        if (lienzo != null) {
            lienzo.setHerramienta(HerramientaDibujo.RECTANGLE);
            this.jLabel1.setText(HerramientaDibujo.RECTANGLE.toString());
            this.turnOffBooleans(lienzo);
        }
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        Lienzo2D lienzo = this.getSelectedLienzo();
        if (lienzo != null) {
            lienzo.setHerramienta(HerramientaDibujo.ELLIPSE);
            this.jLabel1.setText(HerramientaDibujo.ELLIPSE.toString());
            this.turnOffBooleans(lienzo);
        }
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed
        Lienzo2D lienzo = this.getSelectedLienzo();
        if (lienzo != null) {
            lienzo.setHerramienta(HerramientaDibujo.QUADCURVE);
            this.jLabel1.setText(HerramientaDibujo.QUADCURVE.toString());
            this.turnOffBooleans(lienzo);
        }
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed
        Lienzo2D lienzo = this.getSelectedLienzo();
        if (lienzo != null) {
            lienzo.setMover(this.jToggleButton5.isSelected());
            this.jLabel1.setText("MOVING");
            lienzo.setFijar(false);
            lienzo.setBorrar(false);
            lienzo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }//GEN-LAST:event_jToggleButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Lienzo2D lienzo = this.getSelectedLienzo();
        if (lienzo != null) {
            Color color = JColorChooser.showDialog(this, "Elije un color", Color.RED);
            this.jButton1.setBackground(color);
            lienzo.setColor(color);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton6ActionPerformed
        Lienzo2D lienzo = this.getSelectedLienzo();
        if (lienzo != null) {
            lienzo.setRelleno(this.jToggleButton6.isSelected());
        }
    }//GEN-LAST:event_jToggleButton6ActionPerformed

    private void jToggleButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton7ActionPerformed
        Lienzo2D lienzo = this.getSelectedLienzo();
        if (lienzo != null) {
            lienzo.setTransparente(this.jToggleButton7.isSelected());
        }
    }//GEN-LAST:event_jToggleButton7ActionPerformed

    private void jToggleButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton8ActionPerformed
        Lienzo2D lienzo = this.getSelectedLienzo();
        if (lienzo != null) {
            lienzo.setAlisado(this.jToggleButton8.isSelected());
        }
    }//GEN-LAST:event_jToggleButton8ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        Lienzo2D lienzo = this.getSelectedLienzo();
        if (lienzo != null) {
            lienzo.setGrosor(this.jSlider1.getValue());
        }
    }//GEN-LAST:event_jSlider1StateChanged

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JFileChooser dlg = new JFileChooser();
        
        FileNameExtensionFilter filtroImg = new FileNameExtensionFilter("Imagenes "
                + "(.jpg, .png, .gif)", "jpg", "png", "gif");
        dlg.setFileFilter(filtroImg);
        
        int resp = dlg.showOpenDialog(this);
        if (resp == JFileChooser.APPROVE_OPTION) {
            try {
                File f = dlg.getSelectedFile();
                BufferedImage img = ImageIO.read(f);
                String formato = this.getExtension(f);
                VentanaInterna vi = new VentanaInterna(this);
                vi.getLienzo2D().setImg(img);
                vi.setFormatoInicial(formato);
                this.jDesktopPane1.add(vi);
                vi.setTitle(f.getName());
                
                f = new File(getClass().getResource("/sonidos/fijar.wav").getFile());
                vi.getLienzo2D().setSonidoFijar(f);
                f = new File(getClass().getResource("/sonidos/eliminar.wav").getFile());
                vi.getLienzo2D().setSonidoEliminar(f);

                vi.setSize(img.getWidth(), img.getHeight());
                vi.setVisible(true);

                vi.addInternalFrameListener(mvi);
                vi.getLienzo2D().addLienzoListener(ml);
                this.initLienzoByDefault();
                vi.getLienzo2D().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al abrir la imagen:\n" + ex.getMessage(),
                        "Error al abrir archivo",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        VentanaInterna vi = (VentanaInterna) jDesktopPane1.getSelectedFrame();
        if (vi != null) {
            BufferedImage img = vi.getLienzo2D().getPaintedImage();
            if (img != null) {
                JFileChooser dlg = new JFileChooser();
                
                FileNameExtensionFilter filtroJpg = new FileNameExtensionFilter("JPG (*.jpg)", "jpg");
                FileNameExtensionFilter filtroPng = new FileNameExtensionFilter("PNG (*.png)", "png");
                FileNameExtensionFilter filtroGif = new FileNameExtensionFilter("GIF (*.gif)", "gif");
                
                dlg.addChoosableFileFilter(filtroJpg);
                dlg.addChoosableFileFilter(filtroPng);
                dlg.addChoosableFileFilter(filtroGif);
                
                String formatoInicial = vi.getFormatoInicial();
                
                switch (formatoInicial) {
                    case "png":
                        dlg.setFileFilter(filtroPng);
                        break;
                    case "gif":
                        dlg.setFileFilter(filtroGif);
                        break;
                    default:
                        dlg.setFileFilter(filtroJpg);
                        break;
                }
                
                int resp = dlg.showSaveDialog(this);
                if (resp == JFileChooser.APPROVE_OPTION) {
                    try {
                        File f = dlg.getSelectedFile();
                        String formato = this.getExtension(f);
                        
                        if (formato == null) {
                            FileNameExtensionFilter selectedFilter = (FileNameExtensionFilter) dlg.getFileFilter();
                            formato = selectedFilter.getExtensions()[0];
                            f = new File(f.getAbsolutePath() + "." + formato);
                        }
                        
                        ImageIO.write(img, formato, f);
                        vi.setTitle(f.getName());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this,
                                "Error al guardar la imagen:\n" + ex.getMessage(),
                                "Error al guardar archivo",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    
                }
            }
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        NuevoLienzoDialog dialog = new NuevoLienzoDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        
        if (dialog.isAceptado()) {
            int ancho = dialog.getAncho();
            int alto = dialog.getAlto();
            
            BufferedImage img = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, ancho, alto);
            g2d.dispose();
            
            VentanaInterna vi = new VentanaInterna(this);
            jDesktopPane1.add(vi);
            vi.getLienzo2D().setImg(img);
            
            File f = new File(getClass().getResource("/sonidos/fijar.wav").getFile());
            vi.getLienzo2D().setSonidoFijar(f);
            f = new File(getClass().getResource("/sonidos/eliminar.wav").getFile());
            vi.getLienzo2D().setSonidoEliminar(f);
            
            vi.pack();
            vi.setSize(ancho, alto);
            vi.setVisible(true);
            
            vi.addInternalFrameListener(mvi);
            vi.getLienzo2D().addLienzoListener(ml);
            this.initLienzoByDefault();
            vi.getLienzo2D().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jToggleButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton9ActionPerformed
        Lienzo2D lienzo = this.getSelectedLienzo();
        if (lienzo != null) {
            lienzo.setFijar(this.jToggleButton9.isSelected());
            lienzo.setMover(false);
            lienzo.setBorrar(false);
            this.jLabel1.setText("RECORDING");
        }
    }//GEN-LAST:event_jToggleButton9ActionPerformed

    private void jToggleButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton10ActionPerformed
        Lienzo2D lienzo = this.getSelectedLienzo();
        if (lienzo != null) {
            lienzo.setBorrar(this.jToggleButton10.isSelected());
            lienzo.setFijar(false);
            lienzo.setMover(false);
            this.jLabel1.setText("DELETING");
        }
    }//GEN-LAST:event_jToggleButton10ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            BufferedImage img = vi.getLienzo2D().getImg();
            if (img != null) {
                try {
                    RescaleOp rop = new RescaleOp(1.0F, 100.0F, null);
                    rop.filter(img, img);
                    vi.getLienzo2D().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            BufferedImage img = vi.getLienzo2D().getImg();
            if (img != null) {
                try {
                    float filtro[] = {0.1f, 0.1f, 0.1f, 0.1f, 0.2f, 0.1f, 0.1f, 0.1f, 0.1f};
                    Kernel k = new Kernel(3, 3, filtro);
                    ConvolveOp cop = new ConvolveOp(k);
                    BufferedImage imgdest = cop.filter(img, null);
                    vi.getLienzo2D().setImg(imgdest);
                    vi.getLienzo2D().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jSlider2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSlider2FocusGained
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            ColorModel cm = vi.getLienzo2D().getImg().getColorModel();
            WritableRaster raster = vi.getLienzo2D().getImg().copyData(null);
            boolean alfaPre = vi.getLienzo2D().getImg().isAlphaPremultiplied();
            imgFuente = new BufferedImage(cm, raster, alfaPre, null);
        }
    }//GEN-LAST:event_jSlider2FocusGained

    private void jSlider2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSlider2FocusLost
        this.imgFuente = null;
        jSlider2.setValue(0);
    }//GEN-LAST:event_jSlider2FocusLost

    private void jSlider2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider2StateChanged
        if (imgFuente == null) {
            return;
        }

        VentanaInterna vi = (VentanaInterna) jDesktopPane1.getSelectedFrame();
        if (vi != null) {
            RescaleOp rop = new RescaleOp(1.0f, jSlider2.getValue(), null);
            rop.filter(imgFuente, vi.getLienzo2D().getImg());
            jDesktopPane1.repaint();
        }
    }//GEN-LAST:event_jSlider2StateChanged

    private void jSlider3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSlider3FocusGained
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            ColorModel cm = vi.getLienzo2D().getImg().getColorModel();
            WritableRaster raster = vi.getLienzo2D().getImg().copyData(null);
            boolean alfaPre = vi.getLienzo2D().getImg().isAlphaPremultiplied();
            imgFuente = new BufferedImage(cm, raster, alfaPre, null);
        }
    }//GEN-LAST:event_jSlider3FocusGained

    private void jSlider3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSlider3FocusLost
        this.imgFuente = null;
        jSlider3.setValue(10);
    }//GEN-LAST:event_jSlider3FocusLost

    private void jSlider3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider3StateChanged
        if (imgFuente == null) {
            return;
        }

        VentanaInterna vi = (VentanaInterna) jDesktopPane1.getSelectedFrame();
        if (vi != null) {
            RescaleOp rop = new RescaleOp(jSlider3.getValue()/10.0f, 0.0f, null);
            rop.filter(imgFuente, vi.getLienzo2D().getImg());
            vi.getLienzo2D().repaint();
        }
    }//GEN-LAST:event_jSlider3StateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        VentanaInterna vi = (VentanaInterna) jDesktopPane1.getSelectedFrame();
        if (vi != null) {
            BufferedImage img = vi.getLienzo2D().getImg();
            if (img != null) {
                Kernel kernel = null;
                
                switch(jComboBox1.getSelectedIndex()) {
                    case 0:
                        kernel = KernelProducer.createKernel(KernelProducer.TYPE_MEDIA_3x3);
                        break;
                    case 1:
                        kernel = KernelProducer.createKernel(KernelProducer.TYPE_BINOMIAL_3x3);
                        break;
                    case 2:
                        kernel = KernelProducer.createKernel(KernelProducer.TYPE_ENFOQUE_3x3);
                        break;
                    case 3:
                        kernel = KernelProducer.createKernel(KernelProducer.TYPE_RELIEVE_3x3);
                        break;
                    case 4:
                        kernel = KernelProducer.createKernel(KernelProducer.TYPE_LAPLACIANA_3x3);
                        break;
                    case 5:
                        kernel = MiKernelProducer.createKernel(MiKernelProducer.TYPE_MEDIA_5x5);
                        break;
                    case 6:
                        kernel = MiKernelProducer.createKernel(MiKernelProducer.TYPE_MEDIA_7x7);
                        break;
                    case 7:
                        kernel = KernelProducer.createKernel(KernelProducer.TYPE_SOBELX_3x3);
                        break;
                    case 8:
                        kernel = KernelProducer.createKernel(KernelProducer.TYPE_SOBELY_3x3);
                        break;
                }
                
                if (kernel != null) {
                    ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
                    BufferedImage imgdest = cop.filter(img, null);
                    vi.getLienzo2D().setImg(imgdest);
                    vi.getLienzo2D().repaint();
                }
            }
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jSlider4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSlider4FocusGained
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            ColorModel cm = vi.getLienzo2D().getImg().getColorModel();
            WritableRaster raster = vi.getLienzo2D().getImg().copyData(null);
            boolean alfaPre = vi.getLienzo2D().getImg().isAlphaPremultiplied();
            imgFuente = new BufferedImage(cm, raster, alfaPre, null);
        }
    }//GEN-LAST:event_jSlider4FocusGained

    private void jSlider4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSlider4FocusLost
        this.imgFuente = null;
        jSlider4.setValue(1);
    }//GEN-LAST:event_jSlider4FocusLost

    private void jSlider4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider4StateChanged
        VentanaInterna vi = (VentanaInterna) jDesktopPane1.getSelectedFrame();
        if (vi != null && imgFuente != null) {
            Kernel kernel = MiKernelProducer.createKernelMedia(jSlider4.getValue());
            ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            BufferedImage imgdest = cop.filter(imgFuente, null);
            vi.getLienzo2D().setImg(imgdest);
            vi.getLienzo2D().repaint();
        }
    }//GEN-LAST:event_jSlider4StateChanged

    private void jSlider5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSlider5FocusGained
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            ColorModel cm = vi.getLienzo2D().getImg().getColorModel();
            WritableRaster raster = vi.getLienzo2D().getImg().copyData(null);
            boolean alfaPre = vi.getLienzo2D().getImg().isAlphaPremultiplied();
            imgFuente = new BufferedImage(cm, raster, alfaPre, null);
        }
    }//GEN-LAST:event_jSlider5FocusGained

    private void jSlider5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSlider5FocusLost
        this.imgFuente = null;
        jSlider5.setValue(1);
    }//GEN-LAST:event_jSlider5FocusLost

    private void jSlider5StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider5StateChanged
        VentanaInterna vi = (VentanaInterna) jDesktopPane1.getSelectedFrame();
        if (vi != null && imgFuente != null) {
            Kernel kernel = MiKernelProducer.createKernelPerfilado(jSlider5.getValue());
            ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            BufferedImage imgdest = cop.filter(imgFuente, null);
            vi.getLienzo2D().setImg(imgdest);
            vi.getLienzo2D().repaint();
        }
    }//GEN-LAST:event_jSlider5StateChanged

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            BufferedImage img = vi.getLienzo2D().getImg();
            if (img != null) {
                try {
                    float[][] matriz = {{1.0F, 0.0F, 0.0F},
                                        {0.0F, 0.0F, 1.0F},
                                        {0.0F, 1.0F, 0.0F}};
                    BandCombineOp bcop = new BandCombineOp(matriz, null);
                    bcop.filter(img.getRaster(), img.getRaster());
                    vi.getLienzo2D().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            BufferedImage img = vi.getLienzo2D().getImg();
            if (img != null) {
                try {
                    ColorSpace cs = new GreyColorSpace();
                    ColorConvertOp op = new ColorConvertOp(cs, null);
                    BufferedImage imgdest = op.filter(img, null);
                    vi.getLienzo2D().setImg(imgdest);
                    vi.getLienzo2D().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            BufferedImage img = vi.getLienzo2D().getImg();
            if (img != null) {
                try {
                    float[][] matriz = {{0.0F, 0.5F, 0.5F},
                                        {0.5F, 0.0F, 0.5F},
                                        {0.5F, 0.5F, 0.0F}};
                    BandCombineOp bcop = new BandCombineOp(matriz, null);
                    bcop.filter(img.getRaster(), img.getRaster());
                    vi.getLienzo2D().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            BufferedImage img = vi.getLienzo2D().getImg();
            if (img != null) {
                
                for(int i = 0; i < img.getRaster().getNumBands(); i++) {
                    BufferedImage imgBanda = getImageBand(img, i);
                    vi = new VentanaInterna(this);
                    vi.getLienzo2D().setImg(imgBanda);
                    vi.setTitle("[" + i + "]");
                    jDesktopPane1.add(vi);
                    vi.setVisible(true);
                    vi.addInternalFrameListener(mvi);
                    vi.getLienzo2D().addLienzoListener(ml);
                }
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            BufferedImage img = vi.getLienzo2D().getImg();
            if (img != null) {
                ColorSpace cs = null;
                ColorSpace imgCs = img.getColorModel().getColorSpace();
                vi = new VentanaInterna(this);
                switch(jComboBox2.getSelectedIndex()) {
                    case 0:
                        cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
                        vi.setTitle("[sRGB]");
                        break;
                    case 1:
                        cs = ColorSpace.getInstance(ColorSpace.CS_PYCC);
                        vi.setTitle("[YCC]");
                        break;
                    case 2:
                        cs = new GreyColorSpace();
                        vi.setTitle("[GREY]");
                        break;
                }
                
                if (cs.getType() == imgCs.getType() && (!(cs instanceof GreyColorSpace) || 
                        (cs instanceof GreyColorSpace && imgCs instanceof GreyColorSpace))) {
                    System.out.println("La imagen ya está en el espacio de color seleccionado.");
                } else {
                    try {
                        ColorConvertOp op = new ColorConvertOp(cs, null);
                        BufferedImage imgOut = op.filter(img, null);

                        vi.getLienzo2D().setImg(imgOut);
                        jDesktopPane1.add(vi);
                        vi.setVisible(true);
                        vi.addInternalFrameListener(mvi);
                        vi.getLienzo2D().addLienzoListener(ml);
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            BufferedImage img = vi.getLienzo2D().getImg();
            if (img != null) {
                try {
                    AffineTransform at = AffineTransform.getScaleInstance(1.5, 1.5);
                    AffineTransformOp atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                    BufferedImage imgdest = atop.filter(img, null);
                    vi.getLienzo2D().setImg(imgdest);
                    vi.getLienzo2D().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            BufferedImage img = vi.getLienzo2D().getImg();
            if (img != null) {
                try {
                    byte funcionT[] = new byte[256];
                    for (int x = 0; x < 256; x++) {
                        funcionT[x] = (byte) (255 - x); // Negativo
                    }
                    LookupTable tabla = new ByteLookupTable(0, funcionT);
                    LookupOp lop = new LookupOp(tabla, null);
                    lop.filter(img, img);
                    vi.getLienzo2D().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        VentanaInterna vi = (VentanaInterna) jDesktopPane1.getSelectedFrame();
        if (vi != null) {
            BufferedImage img = vi.getLienzo2D().getImg();
            if (img != null) {
                ColorModel cm = img.getColorModel();
                boolean alfaPre = img.isAlphaPremultiplied();
                WritableRaster raster = img.copyData(null);
                BufferedImage imgCopia = new BufferedImage(cm, raster, alfaPre, null);

                VentanaInterna viCopia = new VentanaInterna(this);
                viCopia.getLienzo2D().setImg(imgCopia);
                viCopia.setTitle(vi.getTitle());
                viCopia.setSize(vi.getSize());
                viCopia.setFormatoInicial(vi.getFormatoInicial());

                jDesktopPane1.add(viCopia);
                viCopia.setVisible(true);
                viCopia.addInternalFrameListener(mvi);
                viCopia.getLienzo2D().addLienzoListener(ml);
            }
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        LookupTable tabla = LookupTableProducer.createLookupTable(LookupTableProducer.TYPE_SFUNCION);
        this.aplicarLookup(tabla);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        LookupTable tabla = LookupTableProducer.createLookupTable(LookupTableProducer.TYPE_POWER);
        this.aplicarLookup(tabla);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        LookupTable tabla = LookupTableProducer.createLookupTable(LookupTableProducer.TYPE_LOGARITHM);
        this.aplicarLookup(tabla);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        double r = Math.toRadians(180);
        this.aplicarRotacion(r);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        AffineTransform at = AffineTransform.getScaleInstance(1.25, 1.25);
        this.aplicarEscalado(at);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        AffineTransform at = AffineTransform.getScaleInstance(0.75, 0.75);
        this.aplicarEscalado(at);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jToggleButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton11ActionPerformed
        this.jSlider6.setEnabled(this.jToggleButton11.isSelected());
        this.jSlider7.setEnabled(this.jToggleButton11.isSelected());
        this.jSlider8.setEnabled(this.jToggleButton11.isSelected());
        
        VentanaInterna vi = (VentanaInterna) jDesktopPane1.getSelectedFrame();
        if (vi != null) {
            if (this.jToggleButton11.isSelected()) {
                this.imgFuente = vi.getLienzo2D().getImg();

                this.funcionABC = new DialogoFuncionABC(this);
                this.funcionABC.setABC(this.jSlider6.getValue(), 
                        this.jSlider7.getValue(), this.jSlider8.getValue());
                this.funcionABC.setVisible(true);

                this.actualizarFuncionABC();
            } else {
                this.imgFuente = null;
                if (this.funcionABC != null) {
                    this.funcionABC.setVisible(false);
                }
                this.funcionABC = null;
                this.jSlider6.setValue(0);
                this.jSlider7.setValue(128);
                this.jSlider8.setValue(255);
            }
        }
    }//GEN-LAST:event_jToggleButton11ActionPerformed

    private void jSlider6StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider6StateChanged
        if (this.jToggleButton11.isSelected() && this.funcionABC != null) {
            this.actualizarFuncionABC();
        }
    }//GEN-LAST:event_jSlider6StateChanged

    private void jSlider7StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider7StateChanged
        if (this.jToggleButton11.isSelected()) {
            this.actualizarFuncionABC();
        }
    }//GEN-LAST:event_jSlider7StateChanged

    private void jSlider8StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider8StateChanged
        if (this.jToggleButton11.isSelected()) {
            this.actualizarFuncionABC();
        }
    }//GEN-LAST:event_jSlider8StateChanged

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        LookupTable tabla = MiLookupTableProducer.crearTablaABC(255, 128, 255);
        this.aplicarLookup(tabla);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        LookupTable tabla = null;
        switch (jComboBox3.getSelectedIndex()) {
            case 0:
                tabla = MiLookupTableProducer.crearTablaABC(255, 128, 0);
                break;
            case 1:
                tabla = MiLookupTableProducer.createSepiaTable();
                break;
            case 2:
                tabla = MiLookupTableProducer.crearTablaABC(255, 128, 255);
                break;
            case 3:
                tabla = MiLookupTableProducer.crearTablaABC(0, 128, 0);
                break;
        }
        this.aplicarLookup(tabla);
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            BufferedImage img = vi.getLienzo2D().getImg();
            if (img != null) {
                try {
                    SepiaOp sepia = new SepiaOp();
                    sepia.filter(img, img);
                    vi.getLienzo2D().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jSlider9FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSlider9FocusGained
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            ColorModel cm = vi.getLienzo2D().getImg().getColorModel();
            WritableRaster raster = vi.getLienzo2D().getImg().copyData(null);
            boolean alfaPre = vi.getLienzo2D().getImg().isAlphaPremultiplied();
            imgFuente = new BufferedImage(cm, raster, alfaPre, null);
        }
    }//GEN-LAST:event_jSlider9FocusGained

    private void jSlider9FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSlider9FocusLost
        this.imgFuente = null;
        jSlider9.setValue(2);
    }//GEN-LAST:event_jSlider9FocusLost

    private void jSlider9StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider9StateChanged
        if (imgFuente == null) {
            return;
        }

        VentanaInterna vi = (VentanaInterna) jDesktopPane1.getSelectedFrame();
        if (vi != null) {
            PosterizarOp pop = new PosterizarOp(this.jSlider9.getValue());
            pop.filter(imgFuente, vi.getLienzo2D().getImg());
            vi.getLienzo2D().repaint();
        }
    }//GEN-LAST:event_jSlider9StateChanged

    private void jToggleButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton12ActionPerformed
        this.jSlider10.setEnabled(this.jToggleButton12.isSelected());
        this.jSlider11.setEnabled(this.jToggleButton12.isSelected());
        
        VentanaInterna vi = (VentanaInterna) jDesktopPane1.getSelectedFrame();
        if (vi != null) {
            if (this.jToggleButton12.isSelected()) {
                this.imgFuente = vi.getLienzo2D().getImg();

                this.actualizarCambioTono();
            } else {
                this.imgFuente = null;
                this.jSlider10.setValue(0);
                this.jSlider11.setValue(0);
            }
        }
    }//GEN-LAST:event_jToggleButton12ActionPerformed

    private void jSlider10StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider10StateChanged
        if (this.jToggleButton12.isSelected()) {
            this.actualizarCambioTono();
        }
    }//GEN-LAST:event_jSlider10StateChanged

    private void jSlider11StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider11StateChanged
        if (this.jToggleButton12.isSelected()) {
            this.actualizarCambioTono();
        }
    }//GEN-LAST:event_jSlider11StateChanged

    private void jSlider12StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider12StateChanged
        if (imgFuente == null) {
            return;
        }

        VentanaInterna vi = (VentanaInterna) jDesktopPane1.getSelectedFrame();
        if (vi != null) {
            Color color = this.jButton1.getBackground();
            float gradoMezcla = this.jSlider12.getValue() / 100.0f;
            TintOp tintado = new TintOp(color, gradoMezcla);
            tintado.filter(imgFuente, vi.getLienzo2D().getImg());
            vi.getLienzo2D().repaint();
        }
    }//GEN-LAST:event_jSlider12StateChanged

    private void jSlider12FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSlider12FocusGained
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            ColorModel cm = vi.getLienzo2D().getImg().getColorModel();
            WritableRaster raster = vi.getLienzo2D().getImg().copyData(null);
            boolean alfaPre = vi.getLienzo2D().getImg().isAlphaPremultiplied();
            imgFuente = new BufferedImage(cm, raster, alfaPre, null);
        }
    }//GEN-LAST:event_jSlider12FocusGained

    private void jSlider12FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSlider12FocusLost
        this.imgFuente = null;
        jSlider12.setValue(0);
    }//GEN-LAST:event_jSlider12FocusLost

    private void jSlider13FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSlider13FocusGained
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            ColorModel cm = vi.getLienzo2D().getImg().getColorModel();
            WritableRaster raster = vi.getLienzo2D().getImg().copyData(null);
            boolean alfaPre = vi.getLienzo2D().getImg().isAlphaPremultiplied();
            imgFuente = new BufferedImage(cm, raster, alfaPre, null);
        }
    }//GEN-LAST:event_jSlider13FocusGained

    private void jSlider13StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider13StateChanged
        if (imgFuente != null) {
            this.actualizarRojo();
        }
    }//GEN-LAST:event_jSlider13StateChanged

    private void jSlider13FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSlider13FocusLost
        this.imgFuente = null;
        jSlider13.setValue(0);
    }//GEN-LAST:event_jSlider13FocusLost

    private void jSlider14FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSlider14FocusGained
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            ColorModel cm = vi.getLienzo2D().getImg().getColorModel();
            WritableRaster raster = vi.getLienzo2D().getImg().copyData(null);
            boolean alfaPre = vi.getLienzo2D().getImg().isAlphaPremultiplied();
            imgFuente = new BufferedImage(cm, raster, alfaPre, null);
        }
    }//GEN-LAST:event_jSlider14FocusGained

    private void jSlider14FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSlider14FocusLost
        this.imgFuente = null;
        jSlider14.setValue(128);
    }//GEN-LAST:event_jSlider14FocusLost

    private void jSlider14StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider14StateChanged
        if (imgFuente != null) {
            this.actualizarPopArt();
        }
    }//GEN-LAST:event_jSlider14StateChanged

    private BufferedImage getImageBand(BufferedImage img, int banda) {
        //Creamos el modelo de color de la nueva imagen basado en un espcio de color GRAY
        ColorSpace cs = new GreyColorSpace();
        ComponentColorModel cm = new ComponentColorModel(cs, false, false,
                                                         Transparency.OPAQUE,
                                                         DataBuffer.TYPE_BYTE);
        
        //Creamos el nuevo raster a partir del raster de la imagen original
        int vband[] = {banda};
        WritableRaster bRaster = (WritableRaster) img.getRaster().createWritableChild(0, 0,
                img.getWidth(), img.getHeight(), 0, 0, vband);
        
        //Creamos una nueva imagen que contiene como raster el correspondiente a la banda
        return new BufferedImage(cm, bRaster, false, null);
    }
    
    private void aplicarLookup(LookupTable tabla) {
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            BufferedImage img = vi.getLienzo2D().getImg();
            if (img != null) {
                try {
                    LookupOp lop = new LookupOp(tabla, null);
                    lop.filter(img, img);
                    vi.getLienzo2D().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
        }
    }
    
    private void aplicarRotacion(double r) {
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            BufferedImage img = vi.getLienzo2D().getImg();
            if (img != null) {
                try {
                    Point c = new Point(img.getWidth() / 2, img.getHeight() / 2);
                    AffineTransform at = AffineTransform.getRotateInstance(r, c.x, c.y);
                    AffineTransformOp atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                    BufferedImage imgdest = atop.filter(img, null);
                    vi.getLienzo2D().setImg(imgdest);
                    vi.getLienzo2D().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
        }
    }
    
    private void aplicarEscalado(AffineTransform at) {
        VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
        if (vi != null) {
            BufferedImage img = vi.getLienzo2D().getImg();
            if (img != null) {
                try {
                    AffineTransformOp atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                    BufferedImage imgdest = atop.filter(img, null);
                    vi.getLienzo2D().setImg(imgdest);
                    vi.getLienzo2D().repaint();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
        }
    }
    
    private void actualizarFuncionABC() {
        if (this.imgFuente != null) {
            int a = this.jSlider6.getValue();
            int b = this.jSlider7.getValue();
            int c = this.jSlider8.getValue();

            this.funcionABC.setA(a);
            this.funcionABC.setB(b);
            this.funcionABC.setC(c);

            LookupTable tabla = MiLookupTableProducer.crearTablaABC(a, b, c);

            VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
            if (vi != null) {
                BufferedImage copia = new BufferedImage(
                        this.imgFuente.getColorModel(),
                        this.imgFuente.copyData(null),
                        this.imgFuente.isAlphaPremultiplied(), null
                );
                LookupOp op = new LookupOp(tabla, null);
                op.filter(copia, copia);
                vi.getLienzo2D().setImg(copia);
                vi.getLienzo2D().repaint();
            }
        }
    }
    
    private void actualizarCambioTono() {
        if (this.imgFuente != null) {
            Color color = this.jButton1.getBackground();
            int umbral = this.jSlider10.getValue();
            int desplazamientoTono = this.jSlider11.getValue();
            
            VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
            if (vi != null) {
                BufferedImage copia = new BufferedImage(
                        this.imgFuente.getColorModel(),
                        this.imgFuente.copyData(null),
                        this.imgFuente.isAlphaPremultiplied(), null
                );
                ModificarTonoOp mtop = new ModificarTonoOp(color, umbral, desplazamientoTono);
                mtop.filter(copia, copia);
                vi.getLienzo2D().setImg(copia);
                vi.getLienzo2D().repaint();
            }
        }
    }
    
    private void actualizarRojo() {
        if (this.imgFuente != null) {
            int umbral = this.jSlider13.getValue();
            
            VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
            if (vi != null) {
                BufferedImage copia = new BufferedImage(
                        this.imgFuente.getColorModel(),
                        this.imgFuente.copyData(null),
                        this.imgFuente.isAlphaPremultiplied(), null
                );
                RojoOp rop = new RojoOp(umbral);
                rop.filter(copia, copia);
                vi.getLienzo2D().setImg(copia);
                vi.getLienzo2D().repaint();
            }
        }
    }
    
    private void actualizarPopArt() {
        if (this.imgFuente != null) {
            int umbral = this.jSlider14.getValue();
            Color claro = Color.PINK;
            Color oscuro = Color.BLUE;
            
            VentanaInterna vi = (VentanaInterna) (jDesktopPane1.getSelectedFrame());
            if (vi != null) {
                BufferedImage copia = new BufferedImage(
                        this.imgFuente.getColorModel(),
                        this.imgFuente.copyData(null),
                        this.imgFuente.isAlphaPremultiplied(), null
                );
                PopArtOp paop = new PopArtOp(umbral, claro, oscuro);
                paop.filter(copia, copia);
                vi.getLienzo2D().setImg(copia);
                vi.getLienzo2D().repaint();
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider10;
    private javax.swing.JSlider jSlider11;
    private javax.swing.JSlider jSlider12;
    private javax.swing.JSlider jSlider13;
    private javax.swing.JSlider jSlider14;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JSlider jSlider3;
    private javax.swing.JSlider jSlider4;
    private javax.swing.JSlider jSlider5;
    private javax.swing.JSlider jSlider6;
    private javax.swing.JSlider jSlider7;
    private javax.swing.JSlider jSlider8;
    private javax.swing.JSlider jSlider9;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton10;
    private javax.swing.JToggleButton jToggleButton11;
    private javax.swing.JToggleButton jToggleButton12;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton6;
    private javax.swing.JToggleButton jToggleButton7;
    private javax.swing.JToggleButton jToggleButton8;
    private javax.swing.JToggleButton jToggleButton9;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    // End of variables declaration//GEN-END:variables
}
