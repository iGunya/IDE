package org.example.behaviour;

import org.example.state.params.HairLong;
import org.example.state.params.TypeHaircut;

import static org.example.state.params.TypeHaircut.*;

public class Faice {

    private HairLong headHair;
    private HairLong headWiski;
    private String headTop;
    private String headColor;
    private HairLong beardHairLong;
    private HairLong beardCheeks;
    private HairLong beardChin;
    private HairLong beardMustache;
    private String beardColor;

    private Faice() {
    }

    public HairLong getHeadHair() {
        return headHair;
    }

    public HairLong getHeadWiski() {
        return headWiski;
    }

    public String getHeadTop() {
        return headTop;
    }

    public String getHeadColor() {
        return headColor;
    }

    public HairLong getBeardHairLong() {
        return beardHairLong;
    }

    public HairLong getBeardCheeks() {
        return beardCheeks;
    }

    public HairLong getBeardChin() {
        return beardChin;
    }

    public HairLong getBeardMustache() {
        return beardMustache;
    }

    public String getBeardColor() {
        return beardColor;
    }

    public class FaiceBuilder {

        public FaiceBuilder head( int x, int y, int z ) {
            Faice.this.headHair = getLong( HEAD, x );
            Faice.this.headWiski = getLong( HEAD, z );
            return this;
        }

        private HairLong getLong( TypeHaircut type, int x ) {
            for ( HairLong hairParams : HairLong.values() ) {
                HairLong.LongParams hairLong = hairParams.hairSectorParams.get( type );
                if ( x >= hairLong.min && x <= hairLong.max ) {
                    return hairParams;
                }
            }
            throw new IllegalArgumentException( "Не должна вылететь" );
        }


        public FaiceBuilder headColor() {

        }

        public FaiceBuilder beard( int x, int y, int z ) {
            Faice.this.beardCheeks = getLong( BRARD, x );
            Faice.this.beardChin = getLong( BRARD, y );
            Faice.this.beardMustache = getLong( BRARD, z );
            return this;
        }

        public FaiceBuilder beardColor() {

        }
    }
}
