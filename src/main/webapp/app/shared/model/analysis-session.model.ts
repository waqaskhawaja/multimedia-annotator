import { IAnalysisScenario } from 'app/shared/model/analysis-scenario.model';

export interface IAnalysisSession {
    id?: number;
    name?: string;
    analysisScenario?: IAnalysisScenario;
}

export class AnalysisSession implements IAnalysisSession {
    constructor(public id?: number, public name?: string, public analysisScenario?: IAnalysisScenario) {}
}
