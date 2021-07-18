package adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import Fragments.CommunicationFragment;
import Fragments.GetFoodFragment;
import Fragments.HistoryOrderFragment;
import Fragments.OrderFragment;
import Fragments.WaitingTableFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
            case 1:
                return new OrderFragment();
            case 2:
                return new HistoryOrderFragment();
            case 3:
                return new CommunicationFragment();
            case 5:
                return new WaitingTableFragment();
            default:
                return new GetFoodFragment();

        }
    }
    @Override
    public int getItemCount() {
        return 6;
    }
}
