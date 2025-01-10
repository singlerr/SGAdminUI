package io.github.singlerr.sgadminui.client.ui;

import static icyllis.modernui.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static icyllis.modernui.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import icyllis.modernui.R;
import icyllis.modernui.animation.LayoutTransition;
import icyllis.modernui.animation.ObjectAnimator;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.core.Core;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.BlendMode;
import icyllis.modernui.mc.MuiModApi;
import icyllis.modernui.mc.ui.ThemeControl;
import icyllis.modernui.text.SpannableString;
import icyllis.modernui.text.Spanned;
import icyllis.modernui.text.style.ForegroundColorSpan;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.PagerAdapter;
import icyllis.modernui.widget.ScrollView;
import icyllis.modernui.widget.SeekBar;
import icyllis.modernui.widget.SwitchButton;
import icyllis.modernui.widget.TextView;
import icyllis.modernui.widget.ViewPager;
import io.github.singlerr.sgadminui.client.GameHistory;
import io.github.singlerr.sgadminui.client.GameInfo;
import io.github.singlerr.sgadminui.client.SGAdminUIClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import org.slf4j.helpers.MessageFormatter;

public final class GameHistoryFragment extends Fragment {

  private GameInfo info;

  @NonNull
  public static LinearLayout createCategoryList(Context context,
                                                String name) {
    var layout = new LinearLayout(context);
    layout.setOrientation(LinearLayout.VERTICAL);

    final int dp6 = layout.dp(6);
    final int dp12 = layout.dp(12);
    final int dp18 = layout.dp(18);
    {
      var title = new TextView(context);
      title.setId(R.id.title);
      title.setText(I18n.get(name));
      title.setTextSize(16);
      title.setTextColor(ThemeControl.THEME_COLOR_2);
      title.setTypeface(SGAdminUIClient.NANUM_FONT);
      var params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
      params.gravity = Gravity.START;
      params.setMargins(dp6, dp6, dp6, dp6);
      layout.addView(title, params);
    }

    var params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
    params.gravity = Gravity.CENTER;
    params.setMargins(dp12, dp12, dp12, dp18);
    layout.setLayoutParams(params);

    return layout;
  }

  public static LinearLayout createSwitchLayout(Context context, String name) {
    var layout = new LinearLayout(context);
    layout.setOrientation(LinearLayout.HORIZONTAL);
    layout.setHorizontalGravity(Gravity.START);

    final int dp3 = layout.dp(3);
    final int dp6 = layout.dp(6);
    {
      var title = new TextView(context);
      title.setText(I18n.get(name));
      title.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
      title.setTextSize(14);

      var params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1);
      params.gravity = Gravity.CENTER_VERTICAL;
      layout.addView(title, params);
    }
    {
      var button = new SwitchButton(context);
      button.setId(R.id.button1);
      button.setCheckedColor(ThemeControl.THEME_COLOR);

      var params = new LinearLayout.LayoutParams(layout.dp(36), layout.dp(16));
      params.gravity = Gravity.CENTER_VERTICAL;
      params.setMargins(0, dp3, 0, dp3);
      layout.addView(button, params);
    }

    var params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
    params.gravity = Gravity.CENTER;
    params.setMargins(dp6, 0, dp6, 0);
    layout.setLayoutParams(params);

    String tooltip = name + "_desc";
    if (I18n.exists(tooltip)) {
      layout.setTooltipText(I18n.get(tooltip));
    }

    return layout;
  }

  private static LinearLayout createGuiScaleOption(Context context) {
    var layout = new LinearLayout(context);
    layout.setOrientation(LinearLayout.HORIZONTAL);
    layout.setHorizontalGravity(Gravity.START);

    final int dp3 = layout.dp(3);
    final int dp6 = layout.dp(6);
    {
      var title = new TextView(context);
      title.setText(I18n.get("options.guiScale"));
      title.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
      title.setTextSize(14);

      var params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1);
      params.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
      layout.addView(title, params);
    }

    var slider = new SeekBar(context);
    {
      slider.setClickable(true);
      var params = new LinearLayout.LayoutParams(slider.dp(200), WRAP_CONTENT);
      params.gravity = Gravity.CENTER_VERTICAL;
      layout.addView(slider, params);
    }

    var tv = new TextView(context);
    {
      tv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
      tv.setTextSize(14);
      tv.setPadding(dp3, 0, dp3, 0);
      var params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
      params.gravity = Gravity.CENTER_VERTICAL;
      layout.addView(tv, params);
    }

    int curValue = Minecraft.getInstance().options.guiScale().get();
    tv.setText(guiScaleToString(curValue));
    tv.setMinWidth(slider.dp(50));

    slider.setMax(MuiModApi.MAX_GUI_SCALE);
    slider.setProgress(curValue);
    slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int newValue = seekBar.getProgress();
        tv.setText(guiScaleToString(newValue));
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        int newValue = seekBar.getProgress();
        Core.executeOnMainThread(() -> {
          Minecraft minecraft = Minecraft.getInstance();
          minecraft.options.guiScale().set(newValue);
          // ensure it's applied
          if ((int) minecraft.getWindow().getGuiScale() !=
              minecraft.getWindow().calculateScale(newValue, false)) {
            minecraft.resizeDisplay();
          }
          minecraft.options.save();
        });
        tv.setText(guiScaleToString(newValue));
      }
    });

    var params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
    params.gravity = Gravity.CENTER;
    params.setMargins(dp6, 0, dp6, 0);
    layout.setLayoutParams(params);

    return layout;
  }

  private static CharSequence guiScaleToString(int value) {
    int r = MuiModApi.calcGuiScales();
    if (value == 0) { // auto
      int auto = r >> 4 & 0xf;
      return "A (" + auto + ")";
    } else {
      String valueString = Integer.toString(value);
      int min = r >> 8 & 0xf;
      int max = r & 0xf;
      if (value < min || value > max) {
        final String hint;
        if (value < min) {
          hint = (" (" + min + ")");
        } else {
          hint = (" (" + max + ")");
        }
        var spannableString = new SpannableString(valueString + hint);
        spannableString.setSpan(
            new ForegroundColorSpan(0xFFFF5555),
            0, spannableString.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return spannableString;
      }
      return valueString;
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable DataSet savedInstanceState) {
    var args = getArguments();
    info = (GameInfo) args.get("gameInfo");
    var pager = new ViewPager(getContext());

    pager.setAdapter(this.new ThePagerAdapter());
    pager.setFocusableInTouchMode(true);
    pager.setKeyboardNavigationCluster(true);

    pager.setEdgeEffectColor(ThemeControl.THEME_COLOR);
    pager.setLeftEdgeEffectBlendMode(BlendMode.SRC_OVER);
    pager.setRightEdgeEffectBlendMode(BlendMode.SRC_OVER);

    var lp = new FrameLayout.LayoutParams(pager.dp(720), ViewGroup.LayoutParams.MATCH_PARENT);
    lp.gravity = Gravity.CENTER;
    pager.setLayoutParams(lp);

    return pager;
  }

  public LinearLayout createFirstPage(Context context) {
    var content = new LinearLayout(context);
    content.setOrientation(LinearLayout.VERTICAL);
    var transition = new LayoutTransition();
    transition.enableTransitionType(LayoutTransition.CHANGING);
    content.setLayoutTransition(transition);
    // Screen
    {
      var list = createCategoryList(context, "현재 진행 중인 게임 정보");

      GameInfo.Game currentGame = info.getInfo();
      GameHistory detail = info.getInfo().getCurrentGame();
      var currentGameInfoView = new TextView(getContext());
      currentGameInfoView.setText("현재 진행 중: " + currentGame.getCurrentGameId());
      currentGameInfoView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
      currentGameInfoView.setTextSize(14);
      currentGameInfoView.setTypeface(SGAdminUIClient.NANUM_FONT);

      var gamePlayer = new TextView(getContext());
      gamePlayer.setText(MessageFormatter.basicArrayFormat("생존자 수/플레이어 수 : {}/{}",
          new Object[] {detail.getSurvivorsCount(), detail.getPlayerCount()}));
      gamePlayer.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
      gamePlayer.setTextSize(14);
      gamePlayer.setTypeface(SGAdminUIClient.NANUM_FONT);

      var params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
      params.setMarginsRelative(content.dp(3), content.dp(5), content.dp(3), content.dp(0));

      list.addView(currentGameInfoView, params);
      list.addView(gamePlayer, params);

      content.addView(list);
    }

    {
      var list = createCategoryList(context, "진행 된 게임");

      for (GameHistory game : info.getInfo().getGames()) {
        var params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        params.setMarginsRelative(content.dp(3), content.dp(5), content.dp(3), content.dp(0));

        var gameContent = new LinearLayout(context);
        gameContent.setOrientation(LinearLayout.VERTICAL);
        var innerTransition = new LayoutTransition();
        innerTransition.enableTransitionType(LayoutTransition.CHANGING);

        var gameName = new TextView(getContext());
        gameName.setText("게임 ID: " + game.getId());
        gameName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        gameName.setTextSize(14);
        gameName.setTypeface(SGAdminUIClient.NANUM_FONT);

        var gamePlayer = new TextView(getContext());
        gamePlayer.setText(MessageFormatter.basicArrayFormat("생존자 수/플레이어 수 : {}/{}",
            new Object[] {game.getSurvivorsCount(), game.getPlayerCount()}));
        gamePlayer.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        gamePlayer.setTextSize(14);
        gamePlayer.setTypeface(SGAdminUIClient.NANUM_FONT);


        gameContent.addView(gameName, params);
        gameContent.addView(gamePlayer, params);

        list.addView(gameContent);
      }
      content.addView(list);
    }


    content.setDividerDrawable(ThemeControl.makeDivider(content));
    content.setDividerPadding(content.dp(8));
    content.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

    return content;
  }

  private class ThePagerAdapter extends PagerAdapter {

    @Override
    public int getCount() {
      return 1;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
      var context = container.getContext();
      var sv = new ScrollView(context);
      var animator = ObjectAnimator.ofFloat(sv,
          View.ROTATION_Y, container.isLayoutRtl() ? -45 : 45, 0);
      animator.setInterpolator(TimeInterpolator.DECELERATE);
      sv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
                                   int oldTop, int oldRight, int oldBottom) {
          animator.start();
          v.removeOnLayoutChangeListener(this);
        }
      });
      sv.setEdgeEffectColor(ThemeControl.THEME_COLOR);
      sv.setTopEdgeEffectBlendMode(BlendMode.SRC_OVER);
      sv.setBottomEdgeEffectBlendMode(BlendMode.SRC_OVER);
      sv.addView(createFirstPage(getContext()));
      var params = new LinearLayout.LayoutParams(0, MATCH_PARENT, 1);
      var dp6 = sv.dp(6);
      params.setMargins(dp6, dp6, dp6, dp6);
      container.addView(sv, params);

      return sv;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
      container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
      return view == object;
    }
  }
}
