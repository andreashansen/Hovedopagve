package dk.dampbiksen.community.util;

import java.util.List;

import dk.dampbiksen.community.models.DiscountEntry;
import dk.dampbiksen.community.models.PollEntry;

public interface FirebaseCallback {
    void onCallbackPoll(List<PollEntry> list);
    void onCallbackDiscount(List<DiscountEntry> list);

}
