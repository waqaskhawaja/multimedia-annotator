import { IAnalysisSession } from 'app/shared/model/analysis-session.model';

export interface IAnalysisScenario {
    id?: number;
    name?: string;
    analysisSessions?: IAnalysisSession[];
}

export class AnalysisScenario implements IAnalysisScenario {
    constructor(public id?: number, public name?: string, public analysisSessions?: IAnalysisSession[]) {}
}
