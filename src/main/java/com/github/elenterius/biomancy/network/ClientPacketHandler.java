package com.github.elenterius.biomancy.network;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public final class ClientPacketHandler {

	private ClientPacketHandler() {}

	//	static void handlePacket(BioForgeRecipeClientMessage msg, Supplier<NetworkEvent.Context> ctx) {
	//		if (ctx.get().getNetworkManager().getPacketListener() instanceof ClientPacketListener netHandler) {
	//			ClientLevel level = netHandler.getLevel();
	//			if (level.getBlockEntity(msg.pos) instanceof BioForgeBlockEntity bioForge) {
	//				if (msg.id == null) {
	//					bioForge.setSelectedRecipe(null);
	//					return;
	//				}
	//
	//				RecipeManager recipeManager = level.getRecipeManager();
	//				Map<ResourceLocation, Recipe<Container>> recipes = recipeManager.byType(ModRecipes.BIO_FORGING_RECIPE_TYPE);
	//				bioForge.setSelectedRecipe((BioForgeRecipe) recipes.get(msg.id));
	//			}
	//		}
	//	}

	static void handlePacket(BlockEntityAnimationClientMessage msg, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getNetworkManager().getPacketListener() instanceof ClientPacketListener netHandler) {
			ClientLevel level = netHandler.getLevel();
			if (level.getBlockEntity(msg.pos) instanceof ISyncableAnimation syncable) {
				syncable.onAnimationSync(msg.id, msg.data);
			}
		}
	}

}
