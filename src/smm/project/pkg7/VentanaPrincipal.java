/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package smm.project.pkg7;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBuffer;
import java.awt.image.Kernel;
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
import sm.image.color.GreyColorSpace;
import sm.rlm.enums.HerramientaDibujo;
import sm.rlm.eventos.LienzoAdapter;
import sm.rlm.eventos.LienzoEvent;
import sm.rlm.graficos.MiElipse;
import sm.rlm.graficos.MiRectangulo;
import sm.rlm.graficos.MiShape;
import sm.rlm.graficos.MiShapeRellenable;
import sm.rlm.image.MiKernelProducer;
import sm.rlm.iu.Lienzo2D;

/**
 *
 * @author rober 
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    ManejadorVentanaInterna mvi;
    ManejadorLienzo ml;
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
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();

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

        jPanel2.add(jToolBar2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 533, Short.MAX_VALUE)
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
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jComboBox2ActionPerformed

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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JSlider jSlider3;
    private javax.swing.JSlider jSlider4;
    private javax.swing.JSlider jSlider5;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton10;
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
