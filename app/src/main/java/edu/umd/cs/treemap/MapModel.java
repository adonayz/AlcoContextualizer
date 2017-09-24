package edu.umd.cs.treemap;

/**
 * Model object used by MapLayout to represent
 * data for a edu.umd.cs.treemap.
 */
public interface MapModel {
    /**
     * Get the list of items in this model.
     *
     * @return An array of the Mappable objects in this MapModel.
     */
    public Mappable[] getItems();
}
