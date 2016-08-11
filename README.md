# SkinManager

Control a players skin!

        SkinManager skinManager = SkinManagerPlugin.getSkinManager();
		
		Skin skin = skinManager.createSkin(UUID.fromString("edd09768-3def-4960-b9b0-6521d71df5ac"));
		
		skinManager.setSkin(player, skin);
		
		
Set a skull to any image!

		SkullManager skullManager = SkinManagerPlugin.getSkullManager();
		
		Skin skin = SkinManagerPlugin.getSkinManager().createSkin(UUID.fromString("edd09768-3def-4960-b9b0-6521d71df5ac"));
		
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1);
		
		skullManager.setPlayerSkull(skull, skin);
