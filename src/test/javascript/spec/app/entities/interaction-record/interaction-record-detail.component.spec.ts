/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MaTestModule } from '../../../test.module';
import { InteractionRecordDetailComponent } from 'app/entities/interaction-record/interaction-record-detail.component';
import { InteractionRecord } from 'app/shared/model/interaction-record.model';

describe('Component Tests', () => {
    describe('InteractionRecord Management Detail Component', () => {
        let comp: InteractionRecordDetailComponent;
        let fixture: ComponentFixture<InteractionRecordDetailComponent>;
        const route = ({ data: of({ interactionRecord: new InteractionRecord(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [InteractionRecordDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(InteractionRecordDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(InteractionRecordDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.interactionRecord).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
