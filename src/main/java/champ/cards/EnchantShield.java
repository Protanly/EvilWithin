package champ.cards;

import champ.ChampMod;
import champ.powers.EnchantedShieldPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class EnchantShield extends AbstractChampCard {

    public final static String ID = makeID("EnchantShield");

    //stupid intellij stuff skill, self, rare

    public EnchantShield() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
        tags.add(ChampMod.FINISHER);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        //finisher();
        applyToSelf(new EnchantedShieldPower(1));
        if (upgraded) {
            if (dcombo()) exhaust = false;
        }
        finisher();
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = dcombo() ? GOLD_BORDER_GLOW_COLOR : BLUE_BORDER_GLOW_COLOR;
    }

    public void upp() {
        rawDescription = UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}