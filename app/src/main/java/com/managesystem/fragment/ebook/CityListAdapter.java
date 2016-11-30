package com.managesystem.fragment.ebook;

import android.support.v7.widget.RecyclerView;

import com.managesystem.model.Department;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


/**
 * Adapter holding a list of animal names of type String. Note that each item must be unique.
 */
public abstract class CityListAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {
  private ArrayList<Department> items = new ArrayList<Department>();

  public CityListAdapter() {
    setHasStableIds(true);
  }

  public void add(Department object) {
    items.add(object);
    notifyDataSetChanged();
  }

  public void add(int index, Department object) {
    items.add(index, object);
    notifyDataSetChanged();
  }

  public void addAll(Collection<? extends Department> collection) {
    if (collection != null) {
      items.addAll(collection);
      notifyDataSetChanged();
    }
  }

  public void addAll(Department... items) {
    addAll(Arrays.asList(items));
  }

  public void clear() {
    items.clear();
    notifyDataSetChanged();
  }

  public void remove(String object) {
    items.remove(object);
    notifyDataSetChanged();
  }

  public Department getItem(int position) {
    return items.get(position);
  }

  @Override
  public long getItemId(int position) {
    return getItem(position).hashCode();
  }

  @Override
  public int getItemCount() {
    return items.size();
  }
}
