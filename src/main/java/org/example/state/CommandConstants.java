package org.example.state;

public enum CommandConstants {

    CHOSE_HAIRCUT( "Клиент выбрал стрижку %s" ),
    FOR( "Для %s:" ),
    CURRENT_LONG( "Текущая длинна %s %i милимметров(а)" ),
    DESIRED_LONG( "Желаемая длинна %s %i <числовое значение> миллиметров" ),
    HAIR_COLOR( "Цвет волос %s" ),
    HAIR_STYLING( "Укладка включает %s" ),
    SATISFIED( "Опрос все ли устраивает" ),
    HAIRCUT( "Стрижка %s:" ),
    WASHING_HAIR( "Моем волосы" ),
    HAIRCUT_SECTOR( "Стрижка %s:" ),
    STYLING_PROCESS( "Укладываем волосы" ),
    DRYING_HAIR( "Сушим волосы" );

    public String stingCommand;

    CommandConstants( String stingCommand ) {
        this.stingCommand = stingCommand;
    }
}