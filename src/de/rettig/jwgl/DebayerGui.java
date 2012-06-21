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
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
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


public class DebayerGui extends org.eclipse.swt.widgets.Composite {

	private Menu menu1;
	private Label label1;
	private Scale scaleZoom;
	private Composite composite2;
	private Button button4;
	private Button button3;
	private Button button2;
	private Button button1;
	private Group group1;
	private Composite glComposite;
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
	private Composite composite;
	private Composite composite_1;
	protected int rotation;

	{
		//Register as a resource user - SWTResourceManager will
		//handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}


	public DebayerGui(Composite parent, int style) {
		super(parent, SWT.NONE);
		display = getDisplay();

		initGUI();
		init();
		debayerPlane=new DebayerPlane("res/DSC_0303.png");
		debayerPlane.setGamma(gamma);
		debayerPlane.setFirstRed(new int[]{0,0});
		display.asyncExec(new Runnable() {
			public void run() {
				if (!canvas.isDisposed()) {
					canvas.setCurrent();
					try {
						GLContext.useContext(canvas);
					} catch(LWJGLException e) { e.printStackTrace(); }

					render();
					canvas.swapBuffers();
					display.timerExec(20, this);
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
			getShell().setText("GLSL Demosaic");
			this.setSize(new org.eclipse.swt.graphics.Point(400,300));
			this.setBackground(org.eclipse.wb.swt.SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));

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
							openFileMenuItem.addSelectionListener(new SelectionListener() {

								@Override
								public void widgetSelected(SelectionEvent arg0) {
									FileDialog fileDialog = new FileDialog(getShell(), SWT.OPEN);
									String fileName = fileDialog.open();
									if (fileName!=null){
										debayerPlane.reload(fileName);
									}
								}

								@Override
								public void widgetDefaultSelected(SelectionEvent arg0) {
									// TODO Auto-generated method stub

								}
							});
						}
						{
							exitMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							exitMenuItem.setText("Exit");
						}
						fileMenuItem.setMenu(fileMenu);
					}
				}
				setLayout(new GridLayout(2, false));
				{
					composite_1 = new Composite(this, SWT.NONE);
					composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
					GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 2);
					gd_composite_1.minimumWidth = 100;
					gd_composite_1.widthHint = 106;
					composite_1.setLayoutData(gd_composite_1);

					List list = new List(composite_1, SWT.BORDER);
					list.setItems(new String[] {});
				}
				{
					glComposite = new Composite(this, SWT.NONE);
					glComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					FillLayout composite1Layout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
					glComposite.setLayout(composite1Layout);
				}
				{
					composite1 = new Composite(this, SWT.NONE);
					composite1.setBackground(org.eclipse.wb.swt.SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
					composite1.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1));
					GridLayout composite1Layout = new GridLayout();
					composite1Layout.makeColumnsEqualWidth = true;
					composite1Layout.numColumns = 2;
					composite1.setLayout(composite1Layout);
					{
						composite2 = new Composite(composite1, SWT.NONE);
						FillLayout composite2Layout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
						GridData composite2LData = new GridData();
						composite2LData.widthHint = 120;
						composite2LData.heightHint = 102;
						composite2LData.verticalAlignment = GridData.FILL;
						composite2LData.horizontalAlignment = SWT.LEFT;
						composite2.setLayoutData(composite2LData);
						composite2.setLayout(composite2Layout);
					}
					{
						composite = new Composite(composite1, SWT.NONE);
						composite.setBackground(org.eclipse.wb.swt.SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
						composite.setLayout(new GridLayout(2, false));
						composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
						{
							label1 = new Label(composite, SWT.NONE);
							label1.setBackground(org.eclipse.wb.swt.SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
							label1.setFont(org.eclipse.wb.swt.SWTResourceManager.getFont("Tahoma", 11, SWT.NORMAL));
							label1.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(SWT.COLOR_WHITE));
							label1.setText("Gamma");
						}

						Label lblZoom = new Label(composite, SWT.NONE);
						lblZoom.setBackground(org.eclipse.wb.swt.SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
						lblZoom.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(SWT.COLOR_WHITE));
						lblZoom.setFont(org.eclipse.wb.swt.SWTResourceManager.getFont("Tahoma", 11, SWT.NORMAL));
						lblZoom.setText("Zoom");
						scale1 = new Scale(composite, SWT.HORIZONTAL);
						scale1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
						scale1.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent evt) {
								debayerPlane.setGamma(10f/scale1.getSelection());
							}
						});
						scaleZoom = new Scale(composite, SWT.NONE);
						scaleZoom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
						scaleZoom.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent evt) {
								debayerPlane.setZoom(scaleZoom.getSelection());
							}
						});
						{
							group1 = new Group(composite, SWT.NONE);
							group1.setBackground(org.eclipse.wb.swt.SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
							group1.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(SWT.COLOR_WHITE));
							group1.setFont(org.eclipse.wb.swt.SWTResourceManager.getFont("Tahoma", 11, SWT.NORMAL));
							GridLayout group1Layout = new GridLayout();
							group1Layout.makeColumnsEqualWidth = true;
							group1Layout.numColumns = 2;
							group1.setLayout(group1Layout);
							group1.setText("First red");
							{
								button1 = new Button(group1, SWT.RADIO | SWT.LEFT);
								button1.setSelection(true);
								button1.addSelectionListener(new SelectionAdapter() {

									@Override
									public void widgetSelected(SelectionEvent arg0) {
										if (button1.getSelection()){
											debayerPlane.setFirstRed(new int[]{0,0});
										}
									}

								});
							}
							{
								button2 = new Button(group1, SWT.RADIO | SWT.LEFT);
								button2.addSelectionListener(new SelectionAdapter() {

									@Override
									public void widgetSelected(SelectionEvent arg0) {
										if (button2.getSelection()){
											debayerPlane.setFirstRed(new int[]{0,1});
										}
									}

								});

							}
							{
								button3 = new Button(group1, SWT.RADIO | SWT.LEFT);
								button3.addSelectionListener(new SelectionAdapter() {

									@Override
									public void widgetSelected(SelectionEvent arg0) {
										if (button3.getSelection()){
											debayerPlane.setFirstRed(new int[]{1,0});
										}
									}

								});
							}
							button4 = new Button(group1, SWT.RADIO | SWT.LEFT);

							Button btnRotate = new Button(composite, SWT.NONE);
							btnRotate.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent arg0) {
									rotation += 90;
									debayerPlane.setRotate(rotation);
								}
							});
							btnRotate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
							btnRotate.setText("Rotate");
							button4.addSelectionListener(new SelectionAdapter() {

								@Override
								public void widgetSelected(SelectionEvent arg0) {
									if (button4.getSelection()){
										debayerPlane.setFirstRed(new int[]{1,1});
									}
								}

							});
						}
					}

					{
						scalableHsvColorChooser1 = new ScalableHsvColorChooser(composite2, SWT.NONE);
						scalableHsvColorChooser1.setBackground(org.eclipse.wb.swt.SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
						scalableHsvColorChooser1.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent evt) {
								Color color = scalableHsvColorChooser1.getRGBColor();
								debayerPlane.setWB(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
							}
						});
					}

				}
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init(){
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
					debayerPlane.setWB(1, 1, 1);
					BufferedImage bi = robot.createScreenCapture(new java.awt.Rectangle(
							getShell().getBounds().x + arg0.x + 3,
							getShell().getBounds().y + arg0.y + 22, 10, 10));
					Color c = avgColor(bi);
					float darkest = Math.min(Math.min(c.getRed(), c.getGreen()),c.getBlue());

					scalableHsvColorChooser1.setHSB(java.awt.Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null));
					debayerPlane.setWB(darkest/c.getRed(), darkest/c.getGreen(), darkest/c.getBlue());
				} catch (Exception e) {
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
				//GLU.gluOrtho2D(0.0f, bounds.width, bounds.height, 0.0f);
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
		System.exit(0);
	}
}
