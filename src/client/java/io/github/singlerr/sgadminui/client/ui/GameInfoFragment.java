package io.github.singlerr.sgadminui.client.ui;

import static icyllis.modernui.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static icyllis.modernui.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import icyllis.modernui.R;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.fragment.FragmentContainerView;
import icyllis.modernui.fragment.FragmentTransaction;
import icyllis.modernui.mc.ui.ThemeControl;
import icyllis.modernui.text.Typeface;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.util.StateSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.RadioButton;
import icyllis.modernui.widget.RadioGroup;
import icyllis.modernui.widget.TextView;
import io.github.singlerr.sgadminui.client.GameInfo;
import io.github.singlerr.sgadminui.client.SGAdminUIClient;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.resources.language.I18n;

@RequiredArgsConstructor
public class GameInfoFragment extends Fragment {
  private static final int id_tab_container = 0x2002;
  private static final ColorStateList NAV_BUTTON_COLOR = new ColorStateList(
      new int[][] {
          new int[] {R.attr.state_checked},
          StateSet.get(StateSet.VIEW_STATE_HOVERED),
          StateSet.WILD_CARD},
      new int[] {
          0xFFFFFFFF, // selected
          0xFFE0E0E0, // hovered
          0xFFB4B4B4} // other
  );
  private final GameInfo info;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    getParentFragmentManager().beginTransaction()
        .setPrimaryNavigationFragment(this)
        .commit();
  }

  @Override
  public void onCreate(@Nullable DataSet savedInstanceState) {
    super.onCreate(savedInstanceState);
    var ft = getChildFragmentManager().beginTransaction();
    var args = getArguments();
    DataSet d = new DataSet();
    d.put("gameInfo", info);
    if (args != null && args.getBoolean("navigateToPreferences")) {
      ft.replace(id_tab_container, GameHistoryFragment.class, d, "preferences");
    } else {
      ft.replace(id_tab_container, GamePlayerFragment.class, d, "dashboard");
    }
    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .setReorderingAllowed(true)
        .commit();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable DataSet savedInstanceState) {
    var base = new LinearLayout(getContext());
    base.setOrientation(LinearLayout.VERTICAL);
    base.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
    base.setDividerDrawable(ThemeControl.makeDivider(base));
    base.setDividerPadding(base.dp(8));

    // TITLE
    {
      var title = new TextView(getContext());
      title.setId(R.id.title);
      title.setText("게임 정보");
      title.setTextSize(22);
      title.setTypeface(SGAdminUIClient.NANUM_FONT);
      title.setTextStyle(Typeface.BOLD);

      var params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
      params.gravity = Gravity.CENTER;
      params.setMarginsRelative(0, base.dp(12), 0, base.dp(12));
      base.addView(title, params);
    }

    // NAV BUTTONS
    {
      var buttonGroup = new RadioGroup(getContext());
      buttonGroup.setOrientation(LinearLayout.HORIZONTAL);
      buttonGroup.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

      buttonGroup.addView(createNavButton(1001, "게임 정보"));
      buttonGroup.addView(createNavButton(1002, "플레이어 정보"));
      DataSet d = new DataSet();
      d.put("gameInfo", info);

      buttonGroup.check(1001);
      buttonGroup.setOnCheckedChangeListener((group, checkedId) -> {
        var fm = getChildFragmentManager();
        FragmentTransaction ft = null;
        switch (checkedId) {
          case 1001 -> {
            ft = fm.beginTransaction()
                .replace(id_tab_container, GameHistoryFragment.class, d, "dashboard");
          }
          case 1002 -> {
            ft = fm.beginTransaction()
                .replace(id_tab_container, GamePlayerFragment.class, d, "preferences");
          }
        }
        if (ft != null) {
          ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
              .setReorderingAllowed(true)
              .commit();
        }
      });

      var params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
      params.gravity = Gravity.CENTER;
      base.addView(buttonGroup, params);
    }
    // TAB CONTAINER
    {
      var tabContainer = new FragmentContainerView(getContext());
      tabContainer.setId(id_tab_container);
      var params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
      base.addView(tabContainer, params);
    }

    var params = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
    base.setLayoutParams(params);
    return base;
  }

  private RadioButton createNavButton(int id, String text) {
    var button = new RadioButton(getContext());
    button.setId(id);
    button.setText(I18n.get(text));
    button.setTextSize(16);
    button.setTextColor(NAV_BUTTON_COLOR);
    button.setTypeface(SGAdminUIClient.NANUM_FONT);
    final int dp6 = button.dp(6);
    button.setPadding(dp6, 0, dp6, 0);
    ThemeControl.addBackground(button);

    var params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
    params.setMarginsRelative(dp6 * 3, dp6, dp6 * 3, dp6);
    button.setLayoutParams(params);

    return button;
  }
}
