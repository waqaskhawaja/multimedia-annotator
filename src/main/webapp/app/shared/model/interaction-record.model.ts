import { IInteractionType } from 'app/shared/model/interaction-type.model';
import { IAnalysisSessionResource } from 'app/shared/model/analysis-session-resource.model';
import { IAnnotation } from 'app/shared/model/annotation.model';

export interface IInteractionRecord {
    id?: number;
    duration?: number;
    text?: string;
    sourceId?: string;
    time?: number;
    interactionType?: IInteractionType;
    analysisSessionResource?: IAnalysisSessionResource;
    annotations?: IAnnotation[];
}

export class InteractionRecord implements IInteractionRecord {
    constructor(
        public id?: number,
        public duration?: number,
        public text?: string,
        public sourceId?: string,
        public time?: number,
        public interactionType?: IInteractionType,
        public analysisSessionResource?: IAnalysisSessionResource,
        public annotations?: IAnnotation[]
    ) {}
}
