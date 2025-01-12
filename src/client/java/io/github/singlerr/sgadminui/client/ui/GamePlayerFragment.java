package io.github.singlerr.sgadminui.client.ui;

import static icyllis.modernui.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static icyllis.modernui.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import icyllis.modernui.ModernUI;
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
import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.mc.ModernUIClient;
import icyllis.modernui.mc.MuiModApi;
import icyllis.modernui.mc.UIManager;
import icyllis.modernui.mc.ui.ThemeControl;
import icyllis.modernui.text.Editable;
import icyllis.modernui.text.InputFilter;
import icyllis.modernui.text.SpannableString;
import icyllis.modernui.text.Spanned;
import icyllis.modernui.text.Typeface;
import icyllis.modernui.text.method.DigitsInputFilter;
import icyllis.modernui.text.style.ForegroundColorSpan;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.EditText;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.ImageView;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.PagerAdapter;
import icyllis.modernui.widget.ScrollView;
import icyllis.modernui.widget.SeekBar;
import icyllis.modernui.widget.TextView;
import icyllis.modernui.widget.Toast;
import icyllis.modernui.widget.ViewPager;
import io.github.singlerr.sgadminui.client.GameInfo;
import io.github.singlerr.sgadminui.client.GamePlayer;
import io.github.singlerr.sgadminui.client.SGAdminUIClient;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;

@Slf4j
public final class GamePlayerFragment extends Fragment {

  private GameInfo info;

  private Image adminHeadImage;
  private Image playerHeadImage;

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

  @NonNull
  public static LinearLayout createInputBox(Context context, String name) {
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
      params.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
      layout.addView(title, params);
    }
    {
      var input = new EditText(context);
      input.setId(R.id.input);
      input.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
      input.setTextSize(14);
      input.setPadding(dp3, 0, dp3, 0);

      ThemeControl.addBackground(input);

      var params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
      params.gravity = Gravity.CENTER_VERTICAL;
      layout.addView(input, params);
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

  public static LinearLayout createInputBoxWithSlider(Context context, String name) {
    var layout = createInputBox(context, name);
    var slider = new SeekBar(context);
    slider.setId(R.id.button2);
    slider.setClickable(true);
    var params = new LinearLayout.LayoutParams(slider.dp(200), WRAP_CONTENT);
    params.gravity = Gravity.CENTER_VERTICAL;
    layout.addView(slider, 1, params);
    return layout;
  }


  private static String floatValueToString(float value, float denominator) {
    return Float.toString(Math.round(value * denominator) / denominator);
  }

  public static LinearLayout createFloatOption(Context context, String name,
                                               float minValue, float maxValue, int maxLength,
                                               Supplier<Double> getter, Consumer<Double> setter,
                                               float denominator,
                                               // 10 means step=0.1, 100 means step=0.01
                                               Runnable saveFn) {
    var layout = createInputBoxWithSlider(context, name);
    var slider = layout.<SeekBar>requireViewById(R.id.button2);
    var input = layout.<EditText>requireViewById(R.id.input);
    input.setFilters(DigitsInputFilter.getInstance(null, minValue < 0, true),
        new InputFilter.LengthFilter(maxLength));
    float curValue = getter.get().floatValue();
    input.setText(floatValueToString(curValue, denominator));
    input.setOnFocusChangeListener((view, hasFocus) -> {
      if (!hasFocus) {
        EditText v = (EditText) view;
        float newValue = MathUtil.clamp(Float.parseFloat(v.getText().toString()),
            minValue, maxValue);
        replaceText(v, floatValueToString(newValue, denominator));
        if (newValue != getter.get().floatValue()) {
          setter.accept((double) newValue);
          int curProgress = Math.round((newValue - minValue) * denominator);
          slider.setProgress(curProgress, true);
          saveFn.run();
        }
      }
    });
    input.setMinWidth(slider.dp(50));
    int steps = Math.round((maxValue - minValue) * denominator);
    slider.setMax(steps);
    int curProgress = Math.round((curValue - minValue) * denominator);
    slider.setProgress(curProgress);
    slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float newValue = seekBar.getProgress() / denominator + minValue;
        replaceText(input, floatValueToString(newValue, denominator));
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        float newValue = seekBar.getProgress() / denominator + minValue;
        if (newValue != getter.get().floatValue()) {
          setter.accept((double) newValue);
          replaceText(input, floatValueToString(newValue, denominator));
          saveFn.run();
        }
      }
    });
    return layout;
  }

  public static LinearLayout createStringListOption(Context context,
                                                    String name,
                                                    Runnable saveFn) {
    var option = new LinearLayout(context);
    option.setOrientation(LinearLayout.HORIZONTAL);
    option.setHorizontalGravity(Gravity.START);

    final int dp3 = option.dp(3);
    {
      var title = new TextView(context);
      title.setText(I18n.get(name));
      title.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
      title.setTextSize(14);
      title.setMinWidth(option.dp(60));

      String tooltip = name + "_desc";
      if (I18n.exists(tooltip)) {
        title.setTooltipText(I18n.get(tooltip));
      }

      var params = new LinearLayout.LayoutParams(0, WRAP_CONTENT, 2);
      params.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
      option.addView(title, params);
    }
    {
      var input = new EditText(context);
      input.setId(R.id.input);
      input.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
      input.setTextSize(14);
      input.setPadding(dp3, 0, dp3, 0);

      input.setOnFocusChangeListener((view, hasFocus) -> {
        if (!hasFocus) {
          EditText v = (EditText) view;
          ArrayList<String> result = new ArrayList<>();
          for (String s : v.getText().toString().split("\n")) {
            if (!s.isBlank()) {
              String strip = s.strip();
              if (!strip.isEmpty() && !result.contains(strip)) {
                result.add(strip);
              }
            }
          }
          replaceText(v, String.join("\n", result));
        }
      });

      ThemeControl.addBackground(input);

      var params = new LinearLayout.LayoutParams(0, WRAP_CONTENT, 5);
      params.gravity = Gravity.CENTER_VERTICAL;
      option.addView(input, params);
    }

    var params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
    params.gravity = Gravity.CENTER;
    params.setMargins(option.dp(6), dp3, option.dp(6), dp3);
    option.setLayoutParams(params);

    return option;
  }

  private static void replaceText(@NonNull EditText editText, @NonNull CharSequence newText) {
    Editable text = editText.getText();
    text.replace(0, text.length(), newText);
  }

  private static void reloadDefaultTypeface(@NonNull Context context,
                                            @NonNull Runnable onFontChanged) {
    var future = Minecraft.getInstance().submit(() -> {
      var oldTypeface = ModernUI.getSelectedTypeface();
      var client = ModernUIClient.getInstance();
      client.reloadTypeface();
      client.reloadFontStrike();
      return oldTypeface;
    });
    future.whenCompleteAsync((oldTypeface, throwable) -> {
      if (throwable == null) {
        onFontChanged.run();
        refreshViewTypeface(
            UIManager.getInstance().getDecorView(),
            oldTypeface
        );
        Toast.makeText(context,
            I18n.get("gui.modernui.font_reloaded"),
            Toast.LENGTH_SHORT).show();
      }
    }, Core.getUiThreadExecutor());
  }

  private static void refreshViewTypeface(@NonNull ViewGroup vg,
                                          Typeface oldTypeface) {
    int cc = vg.getChildCount();
    for (int i = 0; i < cc; i++) {
      View v = vg.getChildAt(i);
      if (v instanceof ViewGroup) {
        refreshViewTypeface((ViewGroup) v, oldTypeface);
      } else if (v instanceof TextView tv) {
        if (tv.getTypeface() == oldTypeface) {
          tv.setTypeface(ModernUI.getSelectedTypeface());
        }
      }
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable DataSet savedInstanceState) {
    adminHeadImage = Image.create("sgadminui", "admin.png");
    playerHeadImage = Image.create("sgadminui", "player.png");

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
      var list = createCategoryList(context, "병정 목록");
      var scrollView = new ScrollView(getContext());
      var playerListLayout = new LinearLayout(context);
      playerListLayout.setOrientation(LinearLayout.VERTICAL);
      var playerListLayoutTransition = new LayoutTransition();
      playerListLayoutTransition.enableTransitionType(LayoutTransition.CHANGING);
      playerListLayout.setLayoutTransition(playerListLayoutTransition);

      List<GamePlayer> admins = info.getPlayerList().getPlayerList().stream()
          .filter(p -> p.getRole().equalsIgnoreCase("admin")).toList();

      for (GamePlayer admin : admins) {
        var playerInfoWithHead = new LinearLayout(context);
        playerInfoWithHead.setOrientation(LinearLayout.HORIZONTAL);
        var playerInfoWithHeadTransition = new LayoutTransition();
        playerInfoWithHeadTransition.enableTransitionType(LayoutTransition.CHANGING);
        playerInfoWithHead.setLayoutTransition(playerInfoWithHeadTransition);

        var playerInfo = new LinearLayout(context);
        playerInfo.setOrientation(LinearLayout.VERTICAL);
        var playerInfoTransition = new LayoutTransition();
        playerInfoTransition.enableTransitionType(LayoutTransition.CHANGING);
        playerInfo.setLayoutTransition(playerInfoTransition);

        var params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        params.setMarginsRelative(content.dp(3), content.dp(5), content.dp(3), content.dp(0));

        var headImage = new ImageView(getContext());
        headImage.setImageDrawable(new ImageDrawable(adminHeadImage));
        headImage.setScaleType(ImageView.ScaleType.CENTER);

        headImage.setMinimumHeight(playerInfoWithHead.dp(1f));
        headImage.setMinimumWidth(playerInfoWithHead.dp(1f));
        headImage.setMaxHeight(playerInfoWithHead.dp(30f));
        headImage.setMaxWidth(playerInfoWithHead.dp(30f));
        headImage.setAdjustViewBounds(true);

        var playerName = new TextView(getContext());
        playerName.setText(admin.getDisplayName());
        playerName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        playerName.setTextSize(14);
        playerName.setTypeface(SGAdminUIClient.NANUM_FONT);

        playerInfo.addView(playerName, params);

        var imageParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params.setMarginStart(content.dp(8));


        playerInfoWithHead.addView(headImage, imageParams);
        playerInfoWithHead.addView(playerInfo);

        playerListLayout.addView(playerInfoWithHead);
      }

      scrollView.addView(playerListLayout);
      list.addView(scrollView);
      content.addView(list);
    }

    {
      var list = createCategoryList(context, "플레이어 목록");

      var scrollView = new ScrollView(getContext());
      var playerListLayout = new LinearLayout(context);
      playerListLayout.setOrientation(LinearLayout.VERTICAL);
      var playerListLayoutTransition = new LayoutTransition();
      playerListLayoutTransition.enableTransitionType(LayoutTransition.CHANGING);
      playerListLayout.setLayoutTransition(playerListLayoutTransition);


      List<GamePlayer> players = info.getPlayerList().getPlayerList().stream()
          .filter(p -> !p.getRole().equalsIgnoreCase("admin")).toList();

      for (GamePlayer player : players) {
        var playerInfo = new LinearLayout(context);
        playerInfo.setOrientation(LinearLayout.VERTICAL);
        var playerInfoTransition = new LayoutTransition();
        playerInfoTransition.enableTransitionType(LayoutTransition.CHANGING);
        playerInfo.setLayoutTransition(playerInfoTransition);

        var params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        params.setMarginsRelative(content.dp(3), content.dp(5), content.dp(3), content.dp(0));

        var playerName = new TextView(getContext());
        playerName.setText(player.getDisplayName());
        playerName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        playerName.setTextSize(14);
        playerName.setTypeface(SGAdminUIClient.NANUM_FONT);

        playerInfo.addView(playerName, params);

        playerListLayout.addView(playerInfo);
      }

      scrollView.addView(playerListLayout);
      list.addView(scrollView);
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
