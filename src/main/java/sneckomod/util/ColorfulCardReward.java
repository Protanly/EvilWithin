package sneckomod.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.RewardGlowEffect;
import downfall.downfallMod;
import sneckomod.SneckoMod;

import java.util.ArrayList;
import java.util.Iterator;

public class ColorfulCardReward extends RewardItem {
    public static final String ID = downfallMod.makeID("ColorfulCardReward");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private float REWARD_TEXT_X = 833.0F * Settings.scale;
    private ArrayList<AbstractGameEffect> effects = new ArrayList<>();

    public ColorfulCardReward(AbstractCard.CardColor colorType) {
        this.hb = new Hitbox(460.0F * Settings.scale, 90.0F * Settings.scale);
        this.flashTimer = 0.0F;
        this.isDone = false;
        this.ignoreReward = false;
        this.redText = false;
        this.type = RewardType.CARD;
        if (colorType == AbstractCard.CardColor.COLORLESS) {
            this.cards = AbstractDungeon.getColorlessRewardCards();
        } else if (colorType == AbstractDungeon.player.getCardColor()) {
            this.cards = AbstractDungeon.getRewardCards();
        } else {
            this.cards = getColorfulCards(colorType);
        }

        this.text = TEXT[0] + SneckoMod.getClassFromColor(colorType) + TEXT[1];
        for (AbstractCard c : this.cards)
            if (c.type == AbstractCard.CardType.ATTACK && AbstractDungeon.player.hasRelic("Molten Egg 2")) {
                c.upgrade();
            } else if (c.type == AbstractCard.CardType.SKILL && AbstractDungeon.player.hasRelic("Toxic Egg 2")) {
                c.upgrade();
            } else if (c.type == AbstractCard.CardType.POWER && AbstractDungeon.player.hasRelic("Frozen Egg 2")) {
                c.upgrade();
            }
    }

    public boolean cardListDuplicate(AbstractCard card) {
        for (AbstractCard alreadyHave : this.cards) {
            if (alreadyHave.cardID.equals(card.cardID)) {
                return true;
            }
        }
        return false;
    }

    public void update() {
        if (this.flashTimer > 0.0F) {
            this.flashTimer -= Gdx.graphics.getDeltaTime();
            if (this.flashTimer < 0.0F) {
                this.flashTimer = 0.0F;
            }
        }

        this.hb.update();
        if (this.effects.size() == 0) {
            this.effects.add(new RewardGlowEffect(this.hb.cX, this.hb.cY));
        }

        Iterator i = this.effects.iterator();

        while (i.hasNext()) {
            AbstractGameEffect e = (AbstractGameEffect) i.next();
            e.update();
            if (e.isDone) {
                i.remove();
            }
        }

        if (this.hb.hovered) {
            switch (this.type) {
                case POTION:
                    if (!AbstractDungeon.topPanel.potionCombine) {
                        TipHelper.renderGenericTip(360.0F * Settings.scale, (float) InputHelper.mY, this.potion.name, this.potion.description);
                    }
            }
        }

        if (this.relicLink != null) {
            this.relicLink.redText = this.hb.hovered;
        }

        if (this.hb.justHovered) {
            CardCrawlGame.sound.play("UI_HOVER");
        }

        if (this.hb.hovered && InputHelper.justClickedLeft && !this.isDone) {
            CardCrawlGame.sound.playA("UI_CLICK_1", 0.1F);
            this.hb.clickStarted = true;
        }

        if (this.hb.hovered && CInputActionSet.select.isJustPressed() && !this.isDone) {
            this.hb.clicked = true;
            CardCrawlGame.sound.playA("UI_CLICK_1", 0.1F);
        }

        if (this.hb.clicked) {
            this.hb.clicked = false;
            this.isDone = true;
        }

    }

    @Override
    public void render(SpriteBatch sb) {


        if (this.hb.hovered) {
            sb.setColor(new Color(0.4F, 0.6F, 0.6F, 1.0F));
        } else {
            sb.setColor(new Color(0.5F, 0.6F, 0.6F, 0.8F));
        }

        if (this.hb.clickStarted) {
            sb.draw(ImageMaster.REWARD_SCREEN_ITEM, (float) Settings.WIDTH / 2.0F - 232.0F, this.y - 49.0F, 232.0F, 49.0F, 464.0F, 98.0F, Settings.scale * 0.98F, Settings.scale * 0.98F, 0.0F, 0, 0, 464, 98, false, false);
        } else {
            sb.draw(ImageMaster.REWARD_SCREEN_ITEM, (float) Settings.WIDTH / 2.0F - 232.0F, this.y - 49.0F, 232.0F, 49.0F, 464.0F, 98.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 464, 98, false, false);
        }

        if (this.flashTimer != 0.0F) {
            sb.setColor(0.6F, 1.0F, 1.0F, this.flashTimer * 1.5F);
            sb.setBlendFunction(770, 1);
            sb.draw(ImageMaster.REWARD_SCREEN_ITEM, (float) Settings.WIDTH / 2.0F - 232.0F, this.y - 49.0F, 232.0F, 49.0F, 464.0F, 98.0F, Settings.scale * 1.03F, Settings.scale * 1.15F, 0.0F, 0, 0, 464, 98, false, false);
            sb.setBlendFunction(770, 771);
        }

        Texture cardImg = ImageMaster.REWARD_CARD_NORMAL;

        sb.setColor(Color.WHITE);
        sb.draw(cardImg, REWARD_ITEM_X - 32.0F, this.y - 32.0F - 2.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);


        Color color;
        if (this.hb.hovered) {
            color = Settings.GOLD_COLOR;
        } else {
            color = Settings.CREAM_COLOR;
        }

        if (this.redText) {
            color = Settings.RED_TEXT_COLOR;
        }

        FontHelper.renderSmartText(sb, FontHelper.cardDescFont_N, TEXT[0], REWARD_TEXT_X, this.y + 5.0F * Settings.scale, 1000.0F * Settings.scale, 0.0F, color);
        if (!this.hb.hovered) {

            for (AbstractGameEffect e : effects) {
                e.render(sb);
            }
        }

        this.hb.render(sb);
    }

    public ArrayList<AbstractCard> getColorfulCards(AbstractCard.CardColor c) {
        ArrayList<AbstractCard> colorfulCards = new ArrayList<>();
        ArrayList<AbstractCard> possibleCards = new ArrayList<>();
        for (AbstractCard r : CardLibrary.getAllCards()) {
            if (r.color == c && r.rarity != AbstractCard.CardRarity.SPECIAL && r.rarity != AbstractCard.CardRarity.BASIC) {
                possibleCards.add(r);
            }
        }
        for (int i = 0; i < 3; i++) {
            colorfulCards.add(possibleCards.remove(AbstractDungeon.cardRandomRng.random(0, possibleCards.size() - 1)));
        }
        return colorfulCards;
    }
}