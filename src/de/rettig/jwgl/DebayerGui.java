package de.rettig.jwgl;

import java.awt.Color;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

import com.cloudgarden.resource.SWTResourceManager;

import de.mwa.flashscan.gui.widgets.ScalableHsvColorChooser;

/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class DebayerGui extends org.eclipse.swt.widgets.Composite {

	private Menu menu1;
	private Label label1;
	static private Composite glComposite;
	private MenuItem exitMenuItem;
	private MenuItem openFileMenuItem;
	private Menu fileMenu;
	private MenuItem fileMenuItem;

	private GLCanvas canvas;
	private Display display;
	private DebayerPlane debayerPlane;
	private float gamma = 0.6f;
	private ScalableHsvColorChooser scalableHsvColorChooser1;
	private Scale scale1;
	private Composite composite1;
	protected int mouseDownY = -1;
	protected int mouseDownX = -1;

	{
		//Register as a resource user - SWTResourceManager will
		//handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}

	public DebayerGui(Composite parent, int style) {
		super(parent, style);
		initGUI();
		init();
		debayerPlane=new DebayerPlane("res/pco.png");
		debayerPlane.setGamma(gamma);
		debayerPlane.setFirstRed(new int[]{0,0});

		render();
		display = getDisplay();

		display.asyncExec(new Runnable() {
			public void run() {
				if (!canvas.isDisposed()) {
					canvas.setCurrent();
					try {
						GLContext.useContext(canvas);
					} catch(LWJGLException e) { e.printStackTrace(); }

					render();
					canvas.swapBuffers();
					display.asyncExec(this);
				}
			}
		});
	}

	private void render(){
		try {
			GLContext.useContext(canvas);
		} catch(LWJGLException e) { e.printStackTrace(); }
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		GL11.glLoadIdentity();
		debayerPlane.draw();
	}

	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		try {
			this.setSize(new org.eclipse.swt.graphics.Point(400,300));
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			this.setBackground(SWTResourceManager.getColor(192, 192, 192));

			{
				menu1 = new Menu(getShell(), SWT.BAR);
				getShell().setMenuBar(menu1);
				{
					fileMenuItem = new MenuItem(menu1, SWT.CASCADE);
					fileMenuItem.setText("File");
					{
						fileMenu = new Menu(fileMenuItem);
						{
							openFileMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							openFileMenuItem.setText("Open");
						}
						{
							exitMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							exitMenuItem.setText("Exit");
						}
						fileMenuItem.setMenu(fileMenu);
					}
				}
				{
					glComposite = new Composite(this, SWT.NONE);
					FillLayout composite1Layout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
					glComposite.setLayout(composite1Layout);
					FormData glCompositeLData = new FormData();
					glCompositeLData.width = 390;
					glCompositeLData.height = 133;
					glCompositeLData.left =  new FormAttachment(0, 1000, 5);
					glCompositeLData.bottom =  new FormAttachment(1000, 1000, -162);
					glCompositeLData.top =  new FormAttachment(0, 1000, 5);
					glCompositeLData.right =  new FormAttachment(1000, 1000, -5);
					glComposite.setLayoutData(glCompositeLData);
				}
				{
					composite1 = new Composite(this, SWT.NONE);
					GridLayout composite1Layout = new GridLayout();
					composite1Layout.makeColumnsEqualWidth = true;
					composite1Layout.numColumns = 3;
					FormData composite1LData = new FormData();
					composite1LData.width = 390;
					composite1LData.height = 151;
					composite1LData.bottom =  new FormAttachment(1000, 1000, -5);
					composite1LData.right =  new FormAttachment(1000, 1000, -5);
					composite1LData.left =  new FormAttachment(0, 1000, 5);
					composite1.setLayoutData(composite1LData);
					composite1.setLayout(composite1Layout);
					{
						GridData scale1LData = new GridData();
						scale1 = new Scale(composite1, SWT.NONE);
						scale1.setLayoutData(scale1LData);
						scale1.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent evt) {
								debayerPlane.setGamma(10f/scale1.getSelection());
							}
						});
					}

					{
						GridData scalableHsvColorChooser1LData = new GridData();
						scalableHsvColorChooser1LData.widthHint = 144;
						scalableHsvColorChooser1LData.heightHint = 145;
						scalableHsvColorChooser1 = new ScalableHsvColorChooser(composite1, SWT.NONE);
						scalableHsvColorChooser1.setLayoutData(scalableHsvColorChooser1LData);
						scalableHsvColorChooser1.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent evt) {
								Color color = scalableHsvColorChooser1.getRGBColor();
								debayerPlane.setWB(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
							}
						});
					}
					{
						label1 = new Label(composite1, SWT.NONE);
						GridData label1LData = new GridData();
						label1.setLayoutData(label1LData);
						label1.setText("label1");
					}

				}
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init(){
		int w=1024;
		int h=768;
		GLData data = new GLData ();
		data.doubleBuffer = true;
		canvas = new GLCanvas(glComposite, SWT.NONE, data);
		canvas.addMouseWheelListener(new MouseWheelListener() {
			public void mouseScrolled(MouseEvent evt) {
				debayerPlane.zoom(evt.count*0.1f);
			}
		});
		canvas.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent arg0) {
				mouseDownX = -1;
				mouseDownY = -1;

			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				mouseDownX = arg0.x;
				mouseDownY = arg0.y;

			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				try {
					Robot robot = new Robot();
					BufferedImage bi = robot.createScreenCapture(new java.awt.Rectangle(
									getShell().getBounds().x + arg0.x + 3,
									getShell().getBounds().y + arg0.y + 22, 10, 10));
					Color c = avgColor(bi);
					label1.setBackground(SWTResourceManager.getColor(c.getRed(), c.getGreen(), c.getBlue()));
					debayerPlane.setWB(255f/c.getRed(), 255f/c.getGreen(), 255f/c.getBlue());
					//					ImageIO.write(bi, "jpg", new File("./imageTest.jpg"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		canvas.addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(MouseEvent arg0) {
				if (mouseDownX >= 0){
					debayerPlane.setPos(arg0.x-mouseDownX, mouseDownY-arg0.y);
				}
			}
		});
		canvas.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				Rectangle bounds = canvas.getBounds();
				float fAspect = (float) bounds.width / (float) bounds.height;
				canvas.setCurrent();
				try {
					GLContext.useContext(canvas);
				} catch(LWJGLException e) { e.printStackTrace(); }

				GL11.glViewport(0, 0, bounds.width, bounds.height);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GLU.gluPerspective(45.0f, fAspect, 0.5f, 400.0f);
				//			    GLU.gluOrtho2D(0.0f, bounds.width, bounds.height, 0.0f);
				//GL11.glTranslatef(0.375f, 0.375f,1.0f);
				GL11.glDisable(GL11.GL_DEPTH_TEST);

				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
			}
		});

		canvas.setCurrent();
		try {
			GLContext.useContext(canvas);
		} catch(LWJGLException e) { e.printStackTrace(); }

	}

	protected Color avgColor(BufferedImage bi) {
		long r = 0;
		long b = 0;
		long g = 0;
		for (int x = 0;x<bi.getWidth();x++){
			for (int y = 0;y<bi.getHeight();y++){
				r+=(bi.getRGB(x, y) >> 16) & 0xFF;
				System.out.println(r);
				g+=(bi.getRGB(x, y) >> 8) & 0xFF;
				b+=(bi.getRGB(x, y) ) & 0xFF;
			}	
		}
		int pixels = bi.getHeight()*bi.getWidth();
		return new Color((int)r/pixels,(int)g/pixels,(int)b/pixels);
	}

	/**
	 * Auto-generated main method to display this 
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		DebayerGui inst = new DebayerGui(shell, SWT.NULL);

		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if(size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

}
