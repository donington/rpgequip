package mods.minecraft.donington.superioranvil.common.item.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import mods.minecraft.donington.superioranvil.common.block.SuperiorAnvil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;

public class SuperiorAnvilCraftingManager {
	private static final SuperiorAnvilCraftingManager instance = new SuperiorAnvilCraftingManager();
	private List recipes = new ArrayList();


	public static final SuperiorAnvilCraftingManager getInstance() {
		return instance;
	}


	private SuperiorAnvilCraftingManager() {

		this.recipes.add(new ItemGearSocketRecipe());
		this.recipes.add(new RepairEquipmentRecipe());

		/* debuggin recipes */
		/*
		this.addRecipe(new ItemStack(Items.paper, 3), 3, 1, Items.reeds, Items.reeds, Items.reeds);
		this.addRecipe(new ItemStack(Items.baked_potato, 1), 2, 1, Items.reeds, Items.reeds);
		this.addRecipe(new ItemStack(Blocks.obsidian, 1), 2, 2, Items.reeds, Items.reeds, Items.reeds, Items.reeds);
		this.addRecipe(new ItemStack(Items.lava_bucket, 1), 3, 2, Items.reeds, Items.reeds, Items.reeds, Items.reeds, Items.reeds, Items.reeds);
		this.addRecipe(new ItemStack(Items.fireworks, 1), 3, 3, Items.reeds, Items.reeds, Items.reeds, Items.reeds, Items.reeds, Items.reeds, Items.reeds, Items.reeds, Items.reeds);
		this.addRecipe(new ItemStack(Items.book, 1), 3, 1, Items.reeds, null, Items.reeds);
		this.addShapelessRecipe(new ItemStack(Items.book, 1), Items.paper, Items.paper, Items.paper, Items.leather);
		// dye test
		this.addShapelessRecipe(new ItemStack(Items.apple, 1), new ItemStack(Items.dye, 1, 12), Items.reeds);
		this.addShapelessRecipe(new ItemStack(Items.cake, 1), new ItemStack(Items.dye, 1, 15), Items.reeds);
        */
	}


	/** sort recipe order **/
	public void sortRecipes() {
		Collections.sort(this.recipes, new Comparator() {

			public int compare(ISuperiorAnvilRecipe recipe1, ISuperiorAnvilRecipe recipe2) {
				if ( !(recipe1 instanceof AnvilShapedRecipe) && !(recipe1 instanceof AnvilShapelessRecipe) )
					return -1;
				if ( !(recipe2 instanceof AnvilShapedRecipe) && !(recipe2 instanceof AnvilShapelessRecipe) )
					return -1;
				if ( recipe1 instanceof AnvilShapelessRecipe && recipe2 instanceof AnvilShapedRecipe )
					return 1;
				if ( recipe2 instanceof AnvilShapelessRecipe && recipe1 instanceof AnvilShapedRecipe )
					return -1;
				if ( recipe1.getRecipeSize() < recipe2.getRecipeSize() )
					return 1;
				if ( recipe2.getRecipeSize() < recipe1.getRecipeSize() )
					return -1;

				return 0;
			}

			public int compare(Object obj1, Object obj2) {
				return this.compare((ISuperiorAnvilRecipe)obj1, (ISuperiorAnvilRecipe)obj2);
			}

		});

		for ( Object recipe : this.recipes ) {
			System.out.println(recipe.toString());
		}
	}


	public ISuperiorAnvilRecipe addRecipe(ItemStack stack, int width, int height, Object... data) {
		if ( width < 1 || width > SuperiorAnvil.gridSize || height < 1 || height > SuperiorAnvil.gridSize )
			throw new IndexOutOfBoundsException("invalid recipe dimensions!");
		if ( width * height != data.length )
			throw new IllegalStateException("recipe data does not match recipe dimensions!");

		AnvilShapedRecipe recipe = new AnvilShapedRecipe(stack, width, height, data);
		this.recipes.add(recipe);
		return recipe;
	}

	public ISuperiorAnvilRecipe addShapelessRecipe(ItemStack stack, Object... data) {
		AnvilShapelessRecipe recipe = new AnvilShapelessRecipe(stack, data);
		this.recipes.add(recipe);
		return recipe;
	}

	public ItemStack findMatchingRecipe(InventoryBasic inventory) {

		for (int i = 0; i < this.recipes.size(); i++) {
			ISuperiorAnvilRecipe irecipe = (ISuperiorAnvilRecipe)this.recipes.get(i);

			if (irecipe.matches(inventory)) {
				return irecipe.getCraftingResult(inventory);
			}
		}

		return null;
	}

	/**
	 * returns the List<> of all recipes
	 */
	public List getRecipeList()
	{
		return this.recipes;
	}
}
