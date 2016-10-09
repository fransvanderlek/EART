package org.cocopapaya.eainterface;

import java.util.List;

import net.cmp4oaw.ea_com.common.EA_Collection;

public abstract aspect EA_CollectionAspect<E> {
	
	declare parents : EA_Collection<E> implements List<E>;

}
