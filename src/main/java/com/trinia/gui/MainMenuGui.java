package com.trinia.gui;

import com.google.common.collect.Lists;
import com.trinia.TriniaMod;
import com.trinia.gui.buttons.ButtonCustomGui;
import com.trinia.gui.buttons.ButtonDonate;
import com.trinia.gui.buttons.ButtonFacebook;
import com.trinia.gui.buttons.ButtonPMC;
import com.trinia.gui.buttons.ButtonTwitter;
import com.trinia.gui.buttons.ButtonYoutube;
import com.trinia.handler.tickHandler;
import com.trinia.util.Reference;

import java.awt.Desktop;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonLanguage;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

import scala.actors.threadpool.Arrays;

@SideOnly(Side.CLIENT)
public class MainMenuGui extends GuiScreen implements GuiYesNoCallback {

	private static final AtomicInteger field_175373_f = new AtomicInteger(0);
	private static final Logger logger = LogManager.getLogger();
	private static final Random field_175374_h = new Random();
	/** Counts the number of screen updates. */
	private float updateCounter;
	/** The splash message. */
	private String splashText;
	private float oldMouseX;
	/** The old y position of the mouse pointer */
	private float oldMouseY;
	private float mousePosx;
	private float mousePosY;
	private ButtonCustomGui buttonResetDemo;
	private ButtonCustomGui buttonSingle;
	private ButtonCustomGui buttonMulti;
	private ButtonCustomGui buttonOptions;
	private ButtonCustomGui buttonQuit;
	private ButtonCustomGui buttonCredit;
	private ButtonPMC mediaButtonPMC;
	private ButtonTwitter mediaButtonTwitter;
	private ButtonFacebook mediaButtonFacebook;
	private ButtonYoutube mediaButtonYouTube;
	private ButtonDonate mediaButtonDonate;
	/** Timer used to rotate the panorama, increases every tick. */
	private int panoramaTimer;
	/**
	 * Texture allocated for the current viewport of the main menu's panorama
	 * background.
	 */
	private DynamicTexture viewportTexture;
	private boolean field_175375_v = true;
	/**
	 * The Object object utilized as a thread lock when performing non
	 * thread-safe operations
	 */
	private final Object threadLock = new Object();
	/** OpenGL graphics card warning. */
	private String openGLWarning1;
	/** OpenGL graphics card warning. */
	private String openGLWarning2;
	private String field_104024_v;
	private static final ResourceLocation transBlack = new ResourceLocation(
			TriniaMod.ASSET_PREFIX, "textures/gui/Blue.png");
	private static final ResourceLocation transBlack2 = new ResourceLocation(
			TriniaMod.ASSET_PREFIX, "textures/gui/Blue.png");
	private static final ResourceLocation splashTexts = new ResourceLocation(
			"not used here");
	private static final ResourceLocation minecraftTitleTextures = new ResourceLocation(
			TriniaMod.ASSET_PREFIX, "textures/gui/title/minecraft.png");
	/** An array of all the paths to the panorama pictures. */
	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {
			new ResourceLocation(TriniaMod.ASSET_PREFIX,
					"textures/gui/title/background/panorama_highres_0.png"),
			new ResourceLocation(TriniaMod.ASSET_PREFIX,
					"textures/gui/title/background/panorama_highres_1.png"),
			new ResourceLocation(TriniaMod.ASSET_PREFIX,
					"textures/gui/title/background/panorama_highres_2.png"),
			new ResourceLocation(TriniaMod.ASSET_PREFIX,
					"textures/gui/title/background/panorama_highres_3.png"),
			new ResourceLocation(TriniaMod.ASSET_PREFIX,
					"textures/gui/title/background/panorama_highres_4.png"),
			new ResourceLocation(TriniaMod.ASSET_PREFIX,
					"textures/gui/title/background/panorama_highres_5.png") };
	
	//Player Skin and Name
	private static final ResourceLocation playerBackground = new ResourceLocation(
			TriniaMod.ASSET_PREFIX, "textures/gui/Blue.png");
	private static final ResourceLocation playerHead = new ResourceLocation(
			TriniaMod.ASSET_PREFIX, "textures/gui/temp.png");
	private static final ResourceLocation playerBody = new ResourceLocation(
			TriniaMod.ASSET_PREFIX, "textures/gui/temp.png");
	private static final ResourceLocation playerLeftArm = new ResourceLocation(
			TriniaMod.ASSET_PREFIX, "textures/gui/temp.png");
	private static final ResourceLocation playerRightArm = new ResourceLocation(
			TriniaMod.ASSET_PREFIX, "textures/gui/temp.png");
	private static final ResourceLocation playerLeftLeg = new ResourceLocation(
			TriniaMod.ASSET_PREFIX, "textures/gui/temp.png");
	private static final ResourceLocation playerRightLeg = new ResourceLocation(
			TriniaMod.ASSET_PREFIX, "textures/gui/temp.png");
			
	public static final String field_96138_a = "Please click "
			+ EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET
			+ " for more information.";
	private int field_92024_r;
	private int field_92023_s;
	private int field_92022_t;
	private int field_92021_u;
	private int field_92020_v;
	private int field_92019_w;
	protected String tempText = "";
	protected String TopText = Reference.MENU_TOP_TEXT;
	// "(Somebody write something clever here)"

	protected String versionText = "Trinia version: "
			+ Reference.MENU_TRINIA_VERSION;

	private ResourceLocation field_110351_G;
	private int xSize;
	private int ySize;
	/** Minecraft Realms button. */
	private static final String __OBFID = "CL_00001154";

	public MainMenuGui(EntityPlayer player) {
		this.openGLWarning2 = field_96138_a;

		BufferedReader bufferedreader = null;

		try {
			ArrayList arraylist = Lists.newArrayList();
			bufferedreader = new BufferedReader(new InputStreamReader(Minecraft
					.getMinecraft().getResourceManager()
					.getResource(splashTexts).getInputStream(), Charsets.UTF_8));
			String s;

			while ((s = bufferedreader.readLine()) != null) {
				s = s.trim();

				if (!s.isEmpty()) {
					arraylist.add(s);
				}
			}

			if (!arraylist.isEmpty()) {
				do {
					this.splashText = (String) arraylist.get(field_175374_h
							.nextInt(arraylist.size()));
				} while (this.splashText.hashCode() == 125780783);
			}
		} catch (IOException ioexception1) {
			;
		} finally {
			if (bufferedreader != null) {
				try {
					bufferedreader.close();
				} catch (IOException ioexception) {
					;
				}
			}
		}

		this.updateCounter = field_175374_h.nextFloat();
		this.openGLWarning1 = "";

		if (!GLContext.getCapabilities().OpenGL20
				&& !OpenGlHelper.areShadersSupported()) {
			this.openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
			this.openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
			this.field_104024_v = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
		}
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		++this.panoramaTimer;
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * Fired when a key is typed (except F11 who toggle full screen). This is
	 * the equivalent of KeyListener.keyTyped(KeyEvent e). Args : character
	 * (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		this.viewportTexture = new DynamicTexture(256, 256);
		this.field_110351_G = this.mc.getTextureManager()
				.getDynamicTextureLocation("background", this.viewportTexture);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
			this.splashText = "Happy Holidays from Trinia!!";
		} else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
			this.splashText = "Happy new year from Trinia!!";
		} else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
			this.splashText = "Happy halloween from Trinia!!";
		}

		boolean flag = true;
		int i = this.height / 4 + 48;

		this.addSingleplayerMultiplayerButtons(i, 24);

		Object object = this.threadLock;

		synchronized (this.threadLock) {
			this.field_92023_s = this.fontRendererObj
					.getStringWidth(this.openGLWarning1);
			this.field_92024_r = this.fontRendererObj
					.getStringWidth(this.openGLWarning2);
			int j = Math.max(this.field_92023_s, this.field_92024_r);
			this.field_92022_t = (this.width - j) / 2;
			this.field_92021_u = ((GuiButton) this.buttonList.get(0)).yPosition - 24;
			this.field_92020_v = this.field_92022_t + j;
			this.field_92019_w = this.field_92021_u + 24;
		}
	}
	public void drawButtonForegroundLayer(int mouseX, int mouseY)
    {
        MainMenuGui.this.drawCreativeTabHoveringText(I18n.format("Donate", new Object[0]), 20, 20);
    }
	/**
	 * Adds Singleplayer and Multiplayer buttons on Main Menu for players who
	 * have bought the game.
	 */
	private void addSingleplayerMultiplayerButtons(int p_73969_1_,
			int p_73969_2_) {
		this.buttonList
				.add(this.buttonSingle = new ButtonCustomGui(1, 10, 85, 20, 20,
						I18n.format(Reference.MENU_SINGLEPLAYER, new Object[0])));
		this.buttonList.add(this.buttonMulti = new ButtonCustomGui(2, 10,
				85 + p_73969_2_ * 1, 20, 20, I18n.format(
						Reference.MENU_MULTIPLAYER, new Object[0])));
		this.buttonList.add(this.buttonOptions = new ButtonCustomGui(0, 10,
				85 + p_73969_2_ * 2, 20, 20, I18n.format(
						Reference.MENU_OPTIONS, new Object[0])));
		this.buttonList.add(this.buttonCredit = new ButtonCustomGui(400, 10,
				85 + p_73969_2_ * 3, 20, 20, I18n.format(
						Reference.MENU_CREDITS, new Object[0])));
		this.buttonList.add(this.buttonQuit = new ButtonCustomGui(4, 10,
				85 + p_73969_2_ * 4, 20, 20, I18n.format(Reference.MENU_QUIT,
						new Object[0])));

		this.buttonList.add(this.mediaButtonTwitter = new ButtonTwitter(405,
				this.width - 100, this.height - 25, 20, 20, I18n.format(
						"PMC", new Object[0])));
		this.buttonList.add(this.mediaButtonFacebook = new ButtonFacebook(407,
				this.width - 75, this.height - 25, 20, 20, I18n.format(
						"PMC", new Object[0])));
		this.buttonList.add(this.mediaButtonYouTube = new ButtonYoutube(406,
				this.width - 50, this.height - 25, 20, 20, I18n.format(
						"PMC", new Object[0])));
		this.buttonList.add(this.mediaButtonPMC = new ButtonPMC(404,
				this.width - 25, this.height - 25, 20, 20, I18n.format(
						"PMC", new Object[0])));
		this.buttonList.add(this.mediaButtonDonate = new ButtonDonate(408,
				this.width - 125, this.height - 25, 20, 20, I18n.format(
						"Donate", new Object[0])));
		 
		buttonSingle.width = 126;
		buttonMulti.width = 126;
		buttonOptions.width = 126;
		buttonQuit.width = 126;
		buttonCredit.width = 126;
		mediaButtonPMC.width = 20;
		mediaButtonTwitter.width = 20;
		mediaButtonYouTube.width = 20;
		mediaButtonFacebook.width = 20;
		mediaButtonDonate.width = 20;
		
		buttonSingle.packedFGColour = 16777215;
		buttonMulti.packedFGColour = 16777215;
		buttonOptions.packedFGColour = 16777215;
		buttonQuit.packedFGColour = 16777215;
		buttonCredit.packedFGColour = 16777215;

	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}
		if (button.id == 400) {
			this.mc.displayGuiScreen(new CreditsGUI(this, this.mc.gameSettings));
		}
		if (button.id == 404) {
			try {
				Desktop.getDesktop()
						.browse(new URL(
								"http://www.planetminecraft.com/mod/125-trinia/")
								.toURI());
			} catch (Exception e) {
			}
		}
		if (button.id == 405) {
			try {
				Desktop.getDesktop().browse(
						new URL("https://twitter.com/D_Shillington").toURI());
			} catch (Exception e) {
			}
		}
		if (button.id == 406) {
			try {
				Desktop.getDesktop()
						.browse(new URL(
								"https://www.youtube.com/channel/UCyKpQFIpIIDMolrZ7iAW84w")
								.toURI());
			} catch (Exception e) {
			}
		}
		if (button.id == 407) {
			try {
				Desktop.getDesktop().browse(
						new URL("http://www.facebook.com").toURI());
			} catch (Exception e) {
			}
		}
		if (button.id == 408) {
			try {
				Desktop.getDesktop()
						.browse(new URL(
								"https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=KDDUJTVJG26D2")
								.toURI());
			} catch (Exception e) {
			}
		}
		if (button.id == 5) {
			this.mc.displayGuiScreen(new GuiLanguage(this,
					this.mc.gameSettings, this.mc.getLanguageManager()));
		}

		if (button.id == 1) {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}

		if (button.id == 2) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}

		if (button.id == 4) {
			this.mc.shutdown();
		}

		if (button.id == 6) {
			this.mc.displayGuiScreen(new net.minecraftforge.fml.client.GuiModList(
					this));
		}

		if (button.id == 11) {
			this.mc.launchIntegratedServer("Demo_World", "Demo_World",
					DemoWorldServer.demoWorldSettings);
		}

		if (button.id == 12) {
			ISaveFormat isaveformat = this.mc.getSaveLoader();
			WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

			if (worldinfo != null) {
				GuiYesNo guiyesno = GuiSelectWorld.func_152129_a(this,
						worldinfo.getWorldName(), 12);
				this.mc.displayGuiScreen(guiyesno);
			}
		}
	}

	private void switchToRealms() {
		RealmsBridge realmsbridge = new RealmsBridge();
		realmsbridge.switchToRealms(this);
	}

	public void confirmClicked(boolean result, int id) {
		if (result && id == 12) {
			ISaveFormat isaveformat = this.mc.getSaveLoader();
			isaveformat.flushCache();
			isaveformat.deleteWorldDirectory("Demo_World");
			this.mc.displayGuiScreen(this);
		} else if (id == 13) {
			if (result) {
				try {
					Class oclass = Class.forName("java.awt.Desktop");
					Object object = oclass
							.getMethod("getDesktop", new Class[0]).invoke(
									(Object) null, new Object[0]);
					oclass.getMethod("browse", new Class[] { URI.class })
							.invoke(object,
									new Object[] { new URI(this.field_104024_v) });
				} catch (Throwable throwable) {
					logger.error("Couldn't open link", throwable);
				}
			}

			this.mc.displayGuiScreen(this);
		}
	}

	private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.matrixMode(5889);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.disableCull();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		byte b0 = 8;

		for (int k = 0; k < b0 * b0; ++k) {
			GlStateManager.pushMatrix();
			float f1 = ((float) (k % b0) / (float) b0 - 0.5F) / 64.0F;
			float f2 = ((float) (k / b0) / (float) b0 - 0.5F) / 64.0F;
			float f3 = 0.0F;
			GlStateManager.translate(f1, f2, f3);
			GlStateManager
					.rotate(MathHelper
							.sin(((float) this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F,
							1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(
					-((float) this.panoramaTimer + p_73970_3_) * 0.9F, 0.1F,
					2.0F, 0.0F);

			for (int l = 0; l < 6; ++l) {
				GlStateManager.pushMatrix();

				if (l == 1) {
					GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (l == 2) {
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if (l == 3) {
					GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (l == 4) {
					GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (l == 5) {
					GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				this.mc.getTextureManager().bindTexture(titlePanoramaPaths[l]);
				worldrenderer.startDrawingQuads();
				worldrenderer.setColorRGBA_I(16777215, 255 / (k + 1));
				float f4 = 0.0F;
				worldrenderer.addVertexWithUV(-1.0D, -1.0D, 1.0D,
						(double) (0.0F + f4), (double) (0.0F + f4));
				worldrenderer.addVertexWithUV(1.0D, -1.0D, 1.0D,
						(double) (1.0F - f4), (double) (0.0F + f4));
				worldrenderer.addVertexWithUV(1.0D, 1.0D, 1.0D,
						(double) (1.0F - f4), (double) (1.0F - f4));
				worldrenderer.addVertexWithUV(-1.0D, 1.0D, 1.0D,
						(double) (0.0F + f4), (double) (1.0F - f4));
				tessellator.draw();
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
			GlStateManager.colorMask(true, true, true, false);
		}

		worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.matrixMode(5889);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableDepth();
	}

	/**
	 * Rotate and blurs the skybox view in the main menu
	 */
	protected void drawGuiContainerBackgroundLayer(float partialTicks,
			int mouseX, int mouseY) {

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

	private void rotateAndBlurSkybox(float p_73968_1_) {
		this.mc.getTextureManager().bindTexture(this.field_110351_G);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
				GL11.GL_LINEAR);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.colorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.startDrawingQuads();
		GlStateManager.disableAlpha();
		byte b0 = 0;

		for (int i = 0; i < b0; ++i) {
			worldrenderer
					.setColorRGBA_F(1.0F, 1.0F, 1.0F, 3F / (float) (i + 1));
			int j = this.width;
			int k = this.height;
			float f1 = (float) (i - b0 / 2) / 256.0F;
			worldrenderer.addVertexWithUV((double) j, (double) k,
					(double) this.zLevel, (double) (0.0F + f1), 1.0D);
			worldrenderer.addVertexWithUV((double) j, 0.0D,
					(double) this.zLevel, (double) (1.0F + f1), 1.0D);
			worldrenderer.addVertexWithUV(0.0D, 0.0D, (double) this.zLevel,
					(double) (1.0F + f1), 0.0D);
			worldrenderer.addVertexWithUV(0.0D, (double) k,
					(double) this.zLevel, (double) (0.0F + f1), 0.0D);
		}

		tessellator.draw();
		GlStateManager.enableAlpha();
		GlStateManager.colorMask(true, true, true, true);
	}

	/**
	 * Renders the skybox in the main menu
	 */
	private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
		this.mc.getFramebuffer().unbindFramebuffer();
		GlStateManager.viewport(0, 0, 256, 256);
		this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.mc.getFramebuffer().bindFramebuffer(true);
		GlStateManager.viewport(0, 0, this.mc.displayWidth,
				this.mc.displayHeight);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.startDrawingQuads();
		float f1 = this.width > this.height ? 120.0F / (float) this.width
				: 120.0F / (float) this.height;
		float f2 = (float) this.height * f1 / 256.0F;
		float f3 = (float) this.width * f1 / 256.0F;
		worldrenderer.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
		int k = this.width;
		int l = this.height;
		worldrenderer.addVertexWithUV(0.0D, (double) l, (double) this.zLevel,
				(double) (0.5F - f2), (double) (0.5F + f3));
		worldrenderer.addVertexWithUV((double) k, (double) l,
				(double) this.zLevel, (double) (0.5F - f2),
				(double) (0.5F - f3));
		worldrenderer.addVertexWithUV((double) k, 0.0D, (double) this.zLevel,
				(double) (0.5F + f2), (double) (0.5F - f3));
		worldrenderer.addVertexWithUV(0.0D, 0.0D, (double) this.zLevel,
				(double) (0.5F + f2), (double) (0.5F + f3));
		tessellator.draw();
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		super.drawScreen(mouseX, mouseY, partialTicks);

		this.oldMouseX = (float) mouseX;
		this.oldMouseY = (float) mouseY;
		GlStateManager.disableAlpha();
		this.renderSkybox(mouseX, mouseY, partialTicks);
		GlStateManager.enableAlpha();
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		short short1 = 74;
		int k = -25;
		byte b0 = 30;

		this.drawGradientRect(9000, 0, this.width, this.height, -2130706433,
				16777215);
		this.drawGradientRect(9000, 0, this.width, this.height, 0,
				Integer.MIN_VALUE);
		
		this.mc.getTextureManager().bindTexture(playerBackground);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 0.2f);
		this.drawTexturedModalRect(this.width - 850, 10, 10, 75, 76, 140);
		GL11.glDisable(GL11.GL_BLEND);
		
		this.mc.getTextureManager().bindTexture(playerHead);
		this.drawTexturedModalRect(this.width - 62, 15, 32, 32, 32, 32);

		this.mc.getTextureManager().bindTexture(playerBody);
		this.drawTexturedModalRect(this.width - 62, 47, 80, 80, 32, 48);
		
		this.mc.getTextureManager().bindTexture(playerLeftArm);
		this.drawTexturedModalRect(this.width - 78, 47, 176, 80, 16, 48);
		
		this.mc.getTextureManager().bindTexture(playerRightArm);
		this.drawTexturedModalRect(this.width - 30, 47, 176, 80, 16, 48);
		
		this.mc.getTextureManager().bindTexture(playerLeftLeg);
		this.drawTexturedModalRect(this.width - 62, 95, 16, 80, 16, 48);

		this.mc.getTextureManager().bindTexture(playerRightLeg);
		this.drawTexturedModalRect(this.width - 46, 95, 16, 80, 16, 48);
		
		this.mc.getTextureManager().bindTexture(transBlack);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 0.6f);
		this.drawTexturedModalRect(8, 0, 5, 0, 125, 300);

		this.mc.getTextureManager().bindTexture(transBlack2);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 0.6f);
		this.drawTexturedModalRect(8, 300, 140, 0, 125, 1000);

		this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		this.drawTexturedModalRect(k + 0, b0 + 0, 0, 0, 155, 44);
		this.drawTexturedModalRect(k + 155, b0 + 0, 0, 45, 155, 44);

		worldrenderer.setColorOpaque_I(-1);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) (this.width + -120), 70.0F, 0.0F);
		GlStateManager.rotate(-1.0F, 0.0F, 0.0F, 0.0F);
		float f1 = 1.8F - MathHelper
				.abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L)
						/ 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
		f1 = f1
				* 121.0F
				/ (float) (this.fontRendererObj.getStringWidth(this.splashText) + 52);
		GlStateManager.scale(f1, f1, f1);
		this.drawCenteredString(this.fontRendererObj, this.splashText, -160,
				-45, -256);
		GlStateManager.popMatrix();

		this.drawCenteredString(this.fontRendererObj, this.versionText, - -70,
				this.height - 15, 18668501);
		this.drawCenteredString(this.fontRendererObj, this.tempText,
				this.width / 2 + 56, this.height / 2, -256);

		this.drawCenteredString(this.fontRendererObj, this.TopText, this.width
				/ this.width + 70, this.height / this.height + 10, -256);

		if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0) {
			drawRect(this.field_92022_t - 2, this.field_92021_u - 2,
					this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
			this.drawString(this.fontRendererObj, this.openGLWarning1,
					this.field_92022_t, this.field_92021_u, -1);
			this.drawString(this.fontRendererObj, this.openGLWarning2,
					(this.width - this.field_92024_r) / 2,
					((GuiButton) this.buttonList.get(0)).yPosition - 12, -1);
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		for (int i = 0; i < buttonList.size(); i++) {
	        if (buttonList.get(i) instanceof ButtonDonate) {
	        	ButtonDonate btn = (ButtonDonate) buttonList.get(i);
	                if (btn.isMouseOver()) {
	                        String[] desc = { "Donate" };
	                        java.util.List temp = Arrays.asList(desc);
	                        drawHoveringText(temp, mouseX, mouseY, fontRendererObj);
	                }
	        }
	        if (buttonList.get(i) instanceof ButtonTwitter) {
	        	ButtonTwitter btn = (ButtonTwitter) buttonList.get(i);
	                if (btn.isMouseOver()) {
	                        String[] desc = { "Twitter: Papertazer" };
	                        java.util.List temp = Arrays.asList(desc);
	                        drawHoveringText(temp, mouseX, mouseY, fontRendererObj);
	                }
	        }
	        if (buttonList.get(i) instanceof ButtonFacebook) {
	        	ButtonFacebook btn = (ButtonFacebook) buttonList.get(i);
	                if (btn.isMouseOver()) {
	                        String[] desc = { "SOON: Trinia" };
	                        java.util.List temp = Arrays.asList(desc);
	                        drawHoveringText(temp, mouseX, mouseY, fontRendererObj);
	                }
	        }
	        if (buttonList.get(i) instanceof ButtonYoutube) {
	        	ButtonYoutube btn = (ButtonYoutube) buttonList.get(i);
	                if (btn.isMouseOver()) {
	                        String[] desc = { "Youtube: OMGitsMiniMe" };
	                        java.util.List temp = Arrays.asList(desc);
	                        drawHoveringText(temp, mouseX, mouseY, fontRendererObj);
	                }
	        }
	        if (buttonList.get(i) instanceof ButtonPMC) {
	        	ButtonPMC btn = (ButtonPMC) buttonList.get(i);
	                if (btn.isMouseOver()) {
	                        String[] desc = { "Mod Page" };
	                        java.util.List temp = Arrays.asList(desc);
	                        drawHoveringText(temp, mouseX, mouseY, fontRendererObj);
	                }
	        }
	}
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
			throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		Object object = this.threadLock;

		synchronized (this.threadLock) {
			if (this.openGLWarning1.length() > 0
					&& mouseX >= this.field_92022_t
					&& mouseX <= this.field_92020_v
					&& mouseY >= this.field_92021_u
					&& mouseY <= this.field_92019_w) {
				GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(
						this, this.field_104024_v, 13, true);
				guiconfirmopenlink.disableSecurityWarning();
				this.mc.displayGuiScreen(guiconfirmopenlink);
			}
		}
	}
}