package com.singular.barrister.Interface;

import com.singular.barrister.Model.Client.Client;

public interface RecycleItemClient {
    void onItemClick(Client client);

    void onItemLongClick(Client client);
}