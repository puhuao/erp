package com.managesystem.fragment.ebook;

import android.print.PrinterInfo;
import android.support.v7.widget.RecyclerView;

import com.managesystem.model.Department;
import com.managesystem.model.PersonalInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


/**
 * Adapter holding a list of animal names of type String. Note that each item must be unique.
 */
public abstract class PersonListAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {
  private ArrayList<PersonalInfo> items = new ArrayList<>();

  public PersonListAdapter() {
    setHasStableIds(true);
  }

  public void add(PersonalInfo object) {
    items.add(object);
    notifyDataSetChanged();
  }

  public void add(int index, PersonalInfo object) {
    items.add(index, object);
    notifyDataSetChanged();
  }

  public void addAll(Collection<? extends PersonalInfo> collection) {
    if (collection != null) {
      items.addAll(collection);
      notifyDataSetChanged();
    }
  }

  public void addAll(PersonalInfo... items) {
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

  public PersonalInfo getItem(int position) {
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
