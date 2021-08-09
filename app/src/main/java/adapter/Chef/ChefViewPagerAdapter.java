package adapter.Chef;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import Fragments.Chef.ChefOrderFragment;
import Fragments.Chef.MainChefFragment;

public class ChefViewPagerAdapter extends FragmentStateAdapter {
    private FragmentActivity fragmentActivity;

    public ChefViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new ChefOrderFragment(fragmentActivity);
            default:
                return new MainChefFragment(fragmentActivity);

        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
