package com.github.elenterius.biomancy.client.gui;

import com.github.elenterius.biomancy.BiomancyMod;
import com.github.elenterius.biomancy.util.ClientTextUtil;
import com.github.elenterius.biomancy.util.TextComponentUtil;
import com.github.elenterius.biomancy.world.inventory.menu.DecomposerMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class DecomposerScreen extends AbstractContainerScreen<DecomposerMenu> {

	private static final ResourceLocation BACKGROUND_TEXTURE = BiomancyMod.createRL("textures/gui/menu_decomposer.png");

	public DecomposerScreen(DecomposerMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		imageHeight = 193;
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTick);
		renderTooltip(poseStack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		int posX = imageWidth / 2 - font.width(title) / 2;
		font.draw(poseStack, title, posX, -12, 0xFFFFFF);
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);

		blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		drawProgressBar(poseStack, menu.getCraftingProgressNormalized());
		drawFuelBar(poseStack, menu.getFuelAmountNormalized());
	}

	private void drawProgressBar(PoseStack poseStack, float craftingPct) {
		int uWidth = (int) (craftingPct * 20) + (craftingPct > 0 ? 1 : 0);
		blit(poseStack, leftPos + 75, topPos + 23, 194, 0, uWidth, 2);
	}

	private void drawFuelBar(PoseStack poseStack, float fuelPct) {
		//fuel blob
		int vHeight = (int) (fuelPct * 18) + (fuelPct > 0 ? 1 : 0);
		blit(poseStack, leftPos + 52, topPos + 20 + 18 - vHeight, 176, 18 - vHeight, 18, vHeight);
		//glass highlight
		blit(poseStack, leftPos + 55, topPos + 23, 214, 0, 12, 13);
	}

	@Override
	protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
		if (menu.getCarried().isEmpty()) {
			List<Component> hoveringText = new ArrayList<>();

//			if (GuiUtil.isInRect(leftPos + 75, topPos + 22, 20, 4, mouseX, mouseY)) {
//				float progress = menu.getCraftingProgressNormalized() * 100;
//				hoveringText.add(new TextComponent(progress + "%"));
//			}

			if (GuiUtil.isInRect(leftPos + 52, topPos + 20, 17, 17, mouseX, mouseY)) {
				int amount = menu.getFuelAmount();
				if (amount > 0) {
					DecimalFormat df = ClientTextUtil.getDecimalFormatter("#,###,###");
					hoveringText.add(TextComponentUtil.getTooltipText("nutrients_fuel").append(": " + df.format(amount) + " u"));
				}
				else {
					hoveringText.add(TextComponentUtil.getTooltipText("empty"));
				}
			}

			if (!hoveringText.isEmpty()) {
				renderComponentTooltip(poseStack, hoveringText, mouseX, mouseY);
				return;
			}
		}

		super.renderTooltip(poseStack, mouseX, mouseY);
	}

}
