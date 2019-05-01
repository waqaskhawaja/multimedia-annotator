import { ISession } from 'app/shared/model/session.model';
import { IInteractionType } from 'app/shared/model/interaction-type.model';

export interface IDataRecord {
    id?: number;
    duration?: number;
    text?: string;
    sourceId?: string;
    time?: number;
    session?: ISession;
    interactionType?: IInteractionType;
}

export class DataRecord implements IDataRecord {
    constructor(
        public id?: number,
        public duration?: number,
        public text?: string,
        public sourceId?: string,
        public time?: number,
        public session?: ISession,
        public interactionType?: IInteractionType
    ) {}
}
