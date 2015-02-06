package com.ichmed.trinketeers.util.render;

import java.util.Comparator;

public class GraphicSorterYAxis implements Comparator<IWorldGraphic>
{

	@Override
	public int compare(IWorldGraphic e, IWorldGraphic o)
	{
		return e.getY() > o.getY() ? -1 : e.getY() == o.getY() ? 0 : 1;
	}

}
