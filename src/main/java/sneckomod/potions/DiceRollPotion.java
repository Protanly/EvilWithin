package sneckomod.potions;


import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import sneckomod.actions.NoApplyRandomDamageAction;
import theHexaghost.powers.BurnPower;


public class DiceRollPotion extends CustomPotion {
    public static final String POTION_ID = "sneckomod:DiceRollPotion";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;


    public DiceRollPotion() {
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.SPHERE, PotionColor.FAIRY);
        this.isThrown = true;
        this.targetRequired = true;
    }


    public void initializeData() {
        this.potency = getPotency();
        this.description = (DESCRIPTIONS[0] + this.potency + DESCRIPTIONS[1] + this.potency * 40 + DESCRIPTIONS[2]);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }


    public void use(AbstractCreature target) {
        AbstractDungeon.actionManager.addToBottom(new NoApplyRandomDamageAction(target, this.potency, this.potency * 40, 1, AbstractGameAction.AttackEffect.SMASH));
    }


    public CustomPotion makeCopy() {
        return new DiceRollPotion();
    }

    public int getPotency(int ascensionLevel) {
        return 1;
    }
}


