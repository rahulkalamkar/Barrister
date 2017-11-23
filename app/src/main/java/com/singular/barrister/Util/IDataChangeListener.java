package com.singular.barrister.Util;

/**
 * interface to implement callback mechanism needed for view classes
 *
 * @author simantini.ranade
 */
public interface IDataChangeListener<T extends IModel> {

    public void onDataChanged();

    public void onDataReceived(T response);

    public void onDataFailed(WebServiceError error);
}
