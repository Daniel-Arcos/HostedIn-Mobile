package com.sdi.hostedin.data.callbacks;

import com.sdi.hostedin.data.model.BookedAccommodation;

import java.util.List;

public interface BookedAccommodationsCallBack {
    void onSuccess(List<BookedAccommodation> accommodations, String newToken);
    void onError(String errorMessage, String newToken);
}
