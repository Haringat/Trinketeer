package com.ichmed.trinketeers.util;

import java.util.ArrayList;
import java.util.List;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.entity.pickup.Coin;
import com.ichmed.trinketeers.entity.pickup.HealthBottle;
import com.ichmed.trinketeers.entity.pickup.ManaBottle;
import com.ichmed.trinketeers.entity.pickup.ManaBubble;
import com.ichmed.trinketeers.entity.pickup.SpellScroll;

public class Loot
{
	private static List<LootContainer> lootList = new ArrayList<>();

	static
	{
		lootList.add(new LootContainer()
		{
			@Override
			public int getValue()
			{
				return 2;
			}

			@Override
			public Rarity getRarity()
			{
				return Rarity.BULK;
			}

			@Override
			public Entity createEntity()
			{
				return new ManaBubble();
			}
		});

		lootList.add(new LootContainer()
		{
			@Override
			public int getValue()
			{
				return 50;
			}

			@Override
			public Rarity getRarity()
			{
				return Rarity.COMMON;
			}

			@Override
			public Entity createEntity()
			{
				return new HealthBottle();
			}
		});

		lootList.add(new LootContainer()
		{
			@Override
			public int getValue()
			{
				return 5;
			}

			@Override
			public Rarity getRarity()
			{
				return Rarity.RARE;
			}

			@Override
			public Entity createEntity()
			{
				return new SpellScroll();
			}
		});

		lootList.add(new LootContainer()
		{
			@Override
			public int getValue()
			{
				return 50;
			}

			@Override
			public Rarity getRarity()
			{
				return Rarity.COMMON;
			}

			@Override
			public Entity createEntity()
			{
				return new ManaBottle();
			}
		});

		lootList.add(new LootContainer()
		{
			@Override
			public int getValue()
			{
				return 1;
			}

			@Override
			public Rarity getRarity()
			{
				return Rarity.BULK;
			}

			@Override
			public Entity createEntity()
			{
				return new Coin();
			}
		});
	}

	private static int getRaritySum(List<LootContainer> l)
	{
		int i = 0;
		for (LootContainer c : l)
			i += c.getRarity().randomMod;
		return i;
	}

	public static List<Entity> getLootForValue(int value)
	{
		ArrayList<LootContainer> lTemp = new ArrayList<>(lootList);
		ArrayList<Entity> lResult = new ArrayList<>();

		do
		{
			ArrayList<LootContainer> lTemp2 = new ArrayList<>(lTemp);
			for (LootContainer l : lTemp)
				if (l.getValue() > value) lTemp2.remove(l);
			lTemp = lTemp2;
			double d = Math.random() * getRaritySum(lTemp);
			int c = 0;
			for (int i = 0; i < lTemp.size(); i++)
				if (c + lTemp.get(i).getRarity().randomMod > d)
				{
					lResult.add(lTemp.get(i).createEntity());
					value -= lTemp.get(i).getValue();
					break;
				} else c += lTemp.get(i).getRarity().randomMod;

		} while (lTemp.size() > 0);

		return lResult;
	}

	public static abstract class LootContainer
	{
		public abstract int getValue();

		public abstract Rarity getRarity();

		public abstract Entity createEntity();
	}

	public static enum Rarity
	{
		BULK(200), COMMON(150), UNCOMMON(50), RARE(10), LEGENDARY(1), UNIQUE(1);

		Rarity(int randomMod)
		{
			this.randomMod = randomMod;
		}

		public int randomMod;
	}
}
